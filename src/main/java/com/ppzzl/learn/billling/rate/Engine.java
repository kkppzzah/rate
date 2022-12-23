package com.ppzzl.learn.billling.rate;

import com.ppzzl.learn.billling.model.event.RatedEvent;
import com.ppzzl.learn.billling.model.pricing.EventPricingStrategy;
import com.ppzzl.learn.billling.model.pricing.PricingCombine;
import com.ppzzl.learn.billling.model.pricing.PricingSection;
import com.ppzzl.learn.billling.model.pricing.Tariff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ppzzl.learn.billling.repository.CustomerRepository;
import com.ppzzl.learn.billling.repository.EventRepository;
import com.ppzzl.learn.billling.repository.PricingRepository;
import com.ppzzl.learn.billling.repository.ProductRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 暂不考虑：
 *      跨定价段落处理
 *      定价组合关系(Pricing_Combine_Relation)处理
 *      定价段落关系(Pricing_Section_Relation)处理
 */
@Component
public class Engine {
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final PricingRepository pricingRepository;
    private final EventRepository eventRepository;
    private final PricingSectionMatcher pricingSectionMatcher;
    private final TariffCalculator tariffCalculator;

    @Autowired
    public Engine(ProductRepository productRepository, CustomerRepository customerRepository,
                  PricingRepository pricingRepository, EventRepository eventRepository,
                  PricingSectionMatcher pricingSectionMatcher, TariffCalculator tariffCalculator) {
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.pricingRepository = pricingRepository;
        this.eventRepository = eventRepository;
        this.pricingSectionMatcher = pricingSectionMatcher;
        this.tariffCalculator = tariffCalculator;
    }

    public List<RatedEvent> rate(Context context) {
        // 根据定价计划、事件类型，查找候选的定价组合。
        Collection<PricingCombine> pricingCombines = this.findPricingCombines(
                context.getProduct().getPricingPlanId(), context.getEvent().getSourceEventType()
        );
        // 查找各个定价组合相关的事件定价策略(Event_Pricing_Strategy)
        Collection<EventPricingStrategy> eventPricingStrategies = pricingCombines.stream().map(
                pricingCombine ->
                        this.pricingRepository.getEventPricingStrategy(pricingCombine.getEventPricingStrategyId()).get()
        ).collect(Collectors.toList());
        // 查找所有可用的定价段落(定价段落树的叶子节点)。
        List<PricingSection> pricingSections = new ArrayList<>();
        for (EventPricingStrategy eventPricingStrategy : eventPricingStrategies) {
            Collection<PricingSection> tempPricingSections =
                    this.findAvailablePricingSections(eventPricingStrategy, context);
            if (!tempPricingSections.isEmpty()) {
                pricingSections.addAll(tempPricingSections);
            }
        }
        // 计算定价段落定义的资费。
        List<RatedEvent> ratedEvents = new ArrayList<>();
        for (PricingSection pricingSection : pricingSections) {
            Collection<RatedEvent> tempRatedEvents = this.calculateTariff(pricingSection, context);
            if (!tempRatedEvents.isEmpty()) {
                ratedEvents.addAll(tempRatedEvents);
            }
        }
        return ratedEvents;
    }

    private Collection<PricingCombine> findPricingCombines(long pricingPlanId, long eventTypeId) {
        return this.pricingRepository.findPricingCombineByPlanAndEvent(pricingPlanId, eventTypeId);
    }

    private Collection<PricingSection> findAvailablePricingSections(
            EventPricingStrategy eventPricingStrategy, Context context
    ) {
        // 表示当前正在处理的定价段落。
        PricingSection pricingSection;
        // 表示当前正在处理的定价段落的子定价段落。
        Collection<PricingSection> childPricingSections;
        // 用来存放最终判断为可用的定价段落。
        ArrayList<PricingSection> availablePricingSections = new ArrayList<>();
        // 用来存放尚未处理的定价段落。
        ArrayList<PricingSection> pricingSections = new ArrayList<>();
        // 获取最顶层的定价段落。
        Collection<PricingSection> candidatePricingSections =
                this.pricingRepository.getEventPricingStrategySections(eventPricingStrategy.getEventPricingStrategyId())
                        .get();
        candidatePricingSections = candidatePricingSections.stream().filter(
                ps -> ps.getParentSectionId() == null).collect(Collectors.toList());
        pricingSections.addAll(candidatePricingSections);
        // 查找定价段落树。
        while (!pricingSections.isEmpty()) {
            pricingSection = pricingSections.remove(pricingSections.size() - 1);
            // 判断节点是否匹配。
            if (!this.pricingSectionMatcher.match(pricingSection, context)) {
                continue;
            }
            // 判断是否时叶子节点。
            childPricingSections =
                    this.pricingRepository.getChildPricingSections(pricingSection.getPricingSectionId()).get();
            if (childPricingSections == null || childPricingSections.isEmpty()) {
                availablePricingSections.add(pricingSection);
            } else {
                pricingSections.addAll(childPricingSections);
            }
        }
        return availablePricingSections;
    }

    private Collection<RatedEvent> calculateTariff(PricingSection pricingSection, Context context) {
        Collection<Tariff> tariffs =
                this.pricingRepository.getPricingSectionTariffs(pricingSection.getPricingSectionId()).get();
        List<RatedEvent> ratedEvents = new ArrayList<>();
        for (Tariff tariff : tariffs) {
            RatedEvent ratedEvent = this.tariffCalculator.calculate(tariff, context);
            if (ratedEvent != null) {
                ratedEvents.add(ratedEvent);
            }
        }
        return ratedEvents;
    }
}
