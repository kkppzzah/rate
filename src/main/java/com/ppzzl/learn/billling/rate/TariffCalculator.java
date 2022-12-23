package com.ppzzl.learn.billling.rate;

import com.ppzzl.learn.billling.model.common.TariffType;
import com.ppzzl.learn.billling.model.event.EventAttr;
import com.ppzzl.learn.billling.model.event.RatedEvent;
import com.ppzzl.learn.billling.model.pricing.Tariff;
import org.springframework.stereotype.Component;
import com.ppzzl.learn.billling.repository.PricingRepository;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Component
public class TariffCalculator {
    private final PricingRepository pricingRepository;
    private final RefValueResolver refValueResolver;

    public TariffCalculator(PricingRepository pricingRepository, RefValueResolver refValueResolver) {
        this.pricingRepository = pricingRepository;
        this.refValueResolver = refValueResolver;
    }

    public RatedEvent calculate(Tariff tariff, Context context) {
        TariffType tariffType = tariff.getTariffType();
        if (tariffType.equals(TariffType.ONCE)) {
            return this.calculateTariffOnce(tariff, context);
        }
        if (tariffType.equals(TariffType.USAGE)) {
            return this.calculateTariffUsage(tariff, context);
        }
        return null;
    }

    private RatedEvent calculateTariffOnce(Tariff tariff, Context context) {
        return RatedEvent.builder()
                .acctITemType(tariff.getAcctITemType())
                .product(context.getProduct())
                .fee(new BigDecimal((double)this.refValueResolver.resolve(tariff.getFixedRateValue())))
                .build();
    }

    private RatedEvent calculateTariffUsage(Tariff tariff, Context context) {
        int duration = (Integer) context.getEvent().getEventContent(EventAttr.DURATION).getValue();
        double fixedRateValue = (Double)this.refValueResolver.resolve(tariff.getFixedRateValue());
        double scaledRateValue = (Double)this.refValueResolver.resolve(tariff.getScaledRateValue());
        int rateUnit = tariff.getRateUnit();
        int cyclesNumber = (duration + rateUnit - 1) / rateUnit;
        BigDecimal result = new BigDecimal(fixedRateValue, new MathContext(2, RoundingMode.HALF_UP));
        result = result.add(new BigDecimal(cyclesNumber, new MathContext(2, RoundingMode.HALF_UP))
                .multiply(new BigDecimal(scaledRateValue)));
        return RatedEvent.builder()
                .acctITemType(tariff.getAcctITemType())
                .product(context.getProduct())
                .fee(result)
                .build();
    }
}
