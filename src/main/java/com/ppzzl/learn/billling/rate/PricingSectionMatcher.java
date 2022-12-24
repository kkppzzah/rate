package com.ppzzl.learn.billling.rate;

import com.ppzzl.learn.billling.model.common.PricingSectionType;
import com.ppzzl.learn.billling.model.pricing.PricingRefObject;
import com.ppzzl.learn.billling.model.pricing.PricingSection;
import com.ppzzl.learn.billling.model.pricing.RefValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ppzzl.learn.billling.repository.PricingRepository;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class PricingSectionMatcher {
    private final PricingRepository pricingRepository;
    private final PricingRefObjectResolver pricingRefObjectResolver;
    private final RefValueResolver refValueResolver;
    private static interface RefObjectValueComparator {
        /**
         * 比较。
         * @param refObjectValue 这是定价参考对象解析后的具体值
         * @param refValue 这是引用值解析后的具体值
         * @return
         */
        int compare(Object refObjectValue, Object refValue);
    }
    private static class RefObjectValueTypePair<T, V> {
        private final Class<T> refObjectClass;
        private final Class<V> valueType;

        public RefObjectValueTypePair(Class<T> refObjectClass, Class<V> valueType) {
            this.refObjectClass = refObjectClass;
            this.valueType = valueType;
        }

        public static <T, V> RefObjectValueTypePair of(Class<T> refObjectClass, Class<V> valueType) {
            return new RefObjectValueTypePair(refObjectClass, valueType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.refObjectClass, this.valueType);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj.getClass() != this.getClass()) {
                return false;
            }
            RefObjectValueTypePair other = (RefObjectValueTypePair) obj;
            return this.refObjectClass == other.refObjectClass && this.valueType.equals(other.valueType);
        }
    }
    private static final Map<RefObjectValueTypePair, RefObjectValueComparator> refObjectValueComparators = new HashMap<>();
    static {
        refObjectValueComparators.put(
                RefObjectValueTypePair.of(Date.class, LocalTime.class),
                (refObjectValue, refValue) -> {
                    LocalTime time = (LocalTime) refValue;
                    LocalTime refObjectTime =
                            ((Date)refObjectValue).toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
                    return refObjectTime.compareTo(time);
                }
        );
    }

    @Autowired
    public PricingSectionMatcher(PricingRepository pricingRepository, PricingRefObjectResolver pricingRefObjectResolve,
                                 RefValueResolver refValueResolver) {
        this.pricingRepository = pricingRepository;
        this.pricingRefObjectResolver = pricingRefObjectResolve;
        this.refValueResolver = refValueResolver;
    }

    public boolean match(PricingSection pricingSection, Context context) {
        PricingSectionType sectionType = pricingSection.getSectionType();
        // 无条件段落。
        if (sectionType.equals(PricingSectionType.NO_CONDITION)) {
            return true;
        }
        // 获取定价参考对象。
        Long pricingRefObjectId = pricingSection.getPricingRefObjectId();
        if (pricingRefObjectId == null) {
            return true;
        }
        PricingRefObject pricingRefObject = this.pricingRepository.getPricingRefObject(pricingRefObjectId).get();
        return this.matchRefObject(
                pricingRefObject, pricingSection.getStartRefValue(), pricingSection.getEndRefValue(), context
        );
    }

    private boolean matchRefObject(
            PricingRefObject refObject, RefValue startRefValue, RefValue endRefValue, Context context
    ) {
        Object refObjectValue = this.pricingRefObjectResolver.resolve(refObject, context);

        // 找不到定价参考对象则认为是匹配的。
        if (refObjectValue == null) {
            return true;
        }

        Object startRefValueValue = this.refValueResolver.resolve(startRefValue);
        Object endRefValueValue = this.refValueResolver.resolve(endRefValue);

        if (startRefValueValue == null || endRefValueValue == null) {
            return true;
        }

        RefObjectValueComparator comparator = refObjectValueComparators.get(
                RefObjectValueTypePair.of(refObjectValue.getClass(),
                        (startRefValueValue != null ? startRefValueValue : endRefValueValue).getClass())
        );
        return refObjectValue == null ||
                (
                        (
                                (startRefValueValue == null) ||
                                (startRefValueValue != null &&
                                        comparator.compare(refObjectValue, startRefValueValue) >= 0)
                        ) && (
                                (endRefValueValue == null) ||
                                (endRefValueValue != null &&
                                        comparator.compare(refObjectValue, endRefValueValue) <= 0)
                        )
                );
    }
}
