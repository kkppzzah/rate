package com.ppzzl.learn.billling.app;

import com.ppzzl.learn.billling.config.AppConfig;
import com.ppzzl.learn.billling.model.common.*;
import com.ppzzl.learn.billling.model.customer.Cust;
import com.ppzzl.learn.billling.model.customer.Serv;
import com.ppzzl.learn.billling.model.customer.ServIdentification;
import com.ppzzl.learn.billling.model.event.*;
import com.ppzzl.learn.billling.model.pricing.*;
import com.ppzzl.learn.billling.model.product.Product;
import com.ppzzl.learn.billling.rate.Context;
import com.ppzzl.learn.billling.rate.Engine;
import com.ppzzl.learn.billling.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.ppzzl.learn.billling.repository.CustomerRepository;
import com.ppzzl.learn.billling.repository.EventRepository;
import com.ppzzl.learn.billling.repository.PricingRepository;
import com.ppzzl.learn.billling.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class RateDemo {
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final PricingRepository pricingRepository;
    private final EventRepository eventRepository;
    private final Engine engine;

    @Autowired
    public RateDemo(ProductRepository productRepository, CustomerRepository customerRepository,
                    PricingRepository pricingRepository, EventRepository eventRepository, Engine engine) {
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.pricingRepository = pricingRepository;
        this.eventRepository = eventRepository;
        this.engine = engine;
    }

    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        RateDemo rateDemo = ctx.getBean(RateDemo.class);
        rateDemo.run();
    }

    public void run() {
        // ?????????????????????
        this.prepare();
        // ????????????
        Context context = this.pretreat("19200010001");
        // ?????????
        System.out.println("\n1.0 ????????????????????? - ??????");
        context.setEvent(new Event(this.randomId(), RatableEventType.EVENT_TYPE_ID_INITIAL_INSTALLATION));
        this.dumpRatedEvents(this.engine.rate(context));
        System.out.println();

        System.out.println("2.1 ????????????????????? - ?????? - ?????? - ??????: 34??? ?????????4???/6??? ??????????????????1???/6???");
        context.setEvent(new EventCDR(
                this.randomId(), RatableEventType.EVENT_TYPE_ID_LONG_DISTANCE_CALL,
                EventCDR.CDR.builder()
                        .startTime(DateUtils.createDate(2022, 12, 24, 3, 15, 15))
                        .duration(34)
                        .build()
                ));
        this.dumpRatedEvents(this.engine.rate(context));
        System.out.println();

        System.out.println("2.2 ????????????????????? - ?????? - ?????? - ??????: 34??? ?????????7???/6??? ??????????????????1???/6???");
        context.setEvent(new EventCDR(
                this.randomId(), RatableEventType.EVENT_TYPE_ID_LONG_DISTANCE_CALL,
                EventCDR.CDR.builder()
                        .startTime(DateUtils.createDate(2022, 12, 24, 7, 15, 15))
                        .duration(34)
                        .build()
        ));
        this.dumpRatedEvents(this.engine.rate(context));
        System.out.println();

        System.out.println("2.3 ????????????????????? - ?????? - ??????/???????????? - ?????????");
        System.out.println();

        System.out.println("2.4 ????????????????????? - ?????? - ?????????34??? ?????????3???/6???");
        context.setEvent(new EventCDR(
                this.randomId(), RatableEventType.EVENT_TYPE_ID_LOCAL_CALL,
                EventCDR.CDR.builder()
                        .startTime(DateUtils.createDate(2022, 12, 24, 7, 15, 15))
                        .duration(34)
                        .build()
        ));
        this.dumpRatedEvents(this.engine.rate(context));
        System.out.println();
    }

    private void dumpRatedEvents(List<RatedEvent> ratedEvents) {
        DecimalFormat decimalFormat = new DecimalFormat("#####.##");
        System.out.println("???????????????");
        for (RatedEvent ratedEvent : ratedEvents) {
            System.out.printf(
                    "    ????????????: %s, ???????????????%s, \t????????????: %s, ????????????: %s, ??????: %s\n",
                            ratedEvent.getAcctITemType().getCode(),
                            ratedEvent.getAcctITemType().getName(),
                            ratedEvent.getProduct().getProductCode(),
                            ratedEvent.getProduct().getProductName(),
                            decimalFormat.format(ratedEvent.getFee())
            );
        }
    }

    private long randomId() {
        return ThreadLocalRandom.current().nextLong();
    }

    private Context pretreat(String accNumber) {
        ServIdentification servIdentification = this.customerRepository.getServIdentification(accNumber).get();
        Serv serv = this.customerRepository.getServ(servIdentification.getServId()).get();
        Cust cust = this.customerRepository.getCust(serv.getCustId()).get();
        Product product = this.productRepository.getProduct(serv.getProductId()).get();
        return Context.builder()
                .servIdentification(servIdentification)
                .serv(serv)
                .cust(cust)
                .product(product)
                .build();
    }

    private void prepare() {
        // ????????????
        this.productRepository.addProduct(
                Product.builder()
                        .productId(100001)
                        .billingMode(BillingMode.POSTPAID)
                        .pricingPlanId(5100001)
                        .productCode("PC10000001")
                        .productName("?????????")
                        .productDescription("?????????????????????")
                        .productType(ProductType.MAIN)
                        .productClassification(ProductClassification.MANDATORY)
                        .state(MainState.AVAILABLE)
                        .effDate(DateUtils.createDate(2022, 1, 1))
                        .expDate(DateUtils.createDate(2030, 1, 1))
                        .build()
        );
        // ????????????
        this.customerRepository.addCust(
                Cust.builder()
                        .custId(3000001)
                        .custCode("CC10000001")
                        .custName("??????")
                        .isVip(false)
                        .custType(CustType.PUBLIC)
                        .state(MainState.AVAILABLE)
                        .effDate(DateUtils.createDate(2022, 6, 1))
                        .expDate(DateUtils.createDate(2030, 1, 1))
                        .build()
        );
        this.customerRepository.addServ(
                Serv.builder()
                        .servId(4000001)
                        .agreementId(400000001)
                        .productId(100001)
                        .custId(3000001)
                        .state(ServState.NORMAL)
                        .createDate(DateUtils.createDate(2022, 6, 1))
                        .stateDate(DateUtils.createDate(2022, 6, 1))
                        .build()
        );
        this.customerRepository.addServIdentification(
                ServIdentification.builder()
                        .servId(4000001)
                        .accNbr("19200010001")
                        .effDate(DateUtils.createDate(2022, 6, 1))
                        .expDate(null)
                        .build()
        );
        // ????????????
        this.eventRepository.addRatableEventType(
                RatableEventType.builder()
                        .eventTypeId(RatableEventType.EVENT_TYPE_ID_LONG_DISTANCE_CALL)
                        .eventTypeCode("1100001")
                        .name("????????????????????????")
                        .sumEventType(SumEventType.USAGE)
                        .state(MainState.AVAILABLE)
                        .effDate(DateUtils.createDate(2022, 1, 1))
                        .build()
        );
        this.eventRepository.addRatableEventType(
                RatableEventType.builder()
                        .eventTypeId(RatableEventType.EVENT_TYPE_ID_LOCAL_CALL)
                        .eventTypeCode("1100002")
                        .name("????????????????????????")
                        .sumEventType(SumEventType.USAGE)
                        .state(MainState.AVAILABLE)
                        .effDate(DateUtils.createDate(2022, 1, 1))
                        .build()
        );
        this.eventRepository.addRatableEventType(
                RatableEventType.builder()
                        .eventTypeId(RatableEventType.EVENT_TYPE_ID_INITIAL_INSTALLATION)
                        .eventTypeCode("1200003")
                        .name("??????")
                        .sumEventType(SumEventType.CUSTOMER_ACTION)
                        .state(MainState.AVAILABLE)
                        .effDate(DateUtils.createDate(2022, 1, 1))
                        .build()
        );
        // ????????????
        this.pricingRepository.addTariffUnit(
            TariffUnit.builder()
                    .tariffUnitId(5000101)
                    .measureMethod(MeasureMethod.DURATION)
                    .tariffUnitName("???")
                    .standardConversionRate(1)
                    .build()
        );
        this.pricingRepository.addTariffUnit(
                TariffUnit.builder()
                        .tariffUnitId(5000102)
                        .measureMethod(MeasureMethod.DURATION)
                        .tariffUnitName("???")
                        .standardConversionRate(1)
                        .build()
        );
        this.pricingRepository.addTariffUnit(
                TariffUnit.builder()
                        .tariffUnitId(5000103)
                        .measureMethod(MeasureMethod.DURATION)
                        .tariffUnitName("???")
                        .standardConversionRate(60)
                        .build()
        );
        this.pricingRepository.addOwner(
                Owner.builder()
                        .ownerId(5000001)
                        .ownerObjectType(ObjectType.EVENT)
                        .chargePartyFlag(ChargePartyFlag.CALLLING)
                        .build()
        );
        this.pricingRepository.addPricingRefObject(
                PricingRefObject.builder()
                        .pricingRefObjectId(5001001)
                        .ownerId(5000001)
                        .propertyType(OwnerPropertyType.EVENT)
                        .propertyDefineId(EventAttr.DURATION.getEventAttrId())
                        .build()
        );
        this.pricingRepository.addPricingRefObject(
                PricingRefObject.builder()
                        .pricingRefObjectId(5001002)
                        .ownerId(5000001)
                        .propertyType(OwnerPropertyType.EVENT)
                        .propertyDefineId(EventAttr.START_TIME.getEventAttrId())
                        .build()
        );
        this.pricingRepository.addPricingPlan(
                PricingPlan.builder()
                        .pricingPlanId(5100001)
                        .pricingPlanName("?????????????????????")
                        .pricingDesc("?????????????????????")
                        .build()
        );
        this.pricingRepository.addEventPricingStrategy(
                EventPricingStrategy.builder()
                        .eventPricingStrategyId(5200001)
                        .eventTypeId(110000001)
                        .eventPricingStrategyName("??????????????????????????????")
                        .eventPricingStrategyDesc("??????????????????????????????????????????")
                        .build()
        );
        this.pricingRepository.addEventPricingStrategy(
                EventPricingStrategy.builder()
                        .eventPricingStrategyId(5200002)
                        .eventTypeId(110000002)
                        .eventPricingStrategyName("??????????????????????????????")
                        .eventPricingStrategyDesc("??????????????????????????????????????????")
                        .build()
        );
        this.pricingRepository.addEventPricingStrategy(
                EventPricingStrategy.builder()
                        .eventPricingStrategyId(5200003)
                        .eventTypeId(120000003)
                        .eventPricingStrategyName("?????????")
                        .eventPricingStrategyDesc("?????????????????????")
                        .build()
        );
        this.pricingRepository.addPricingCombine(
                PricingCombine.builder()
                        .pricingCombineId(5201001)
                        .pricingPlanId(5100001)
                        .eventPricingStrategyId(5200001)
                        .lifeCycle(
                                LifeCycle.builder()
                                        .state(MainState.AVAILABLE)
                                        .effDate(DateUtils.createDate(2022, 1, 1))
                                        .build())
                        .build()
        );
        this.pricingRepository.addPricingCombine(
                PricingCombine.builder()
                        .pricingCombineId(5201002)
                        .pricingPlanId(5100001)
                        .eventPricingStrategyId(5200002)
                        .lifeCycle(
                                LifeCycle.builder()
                                        .state(MainState.AVAILABLE)
                                        .effDate(DateUtils.createDate(2022, 1, 1))
                                        .build())
                        .build()
        );
        this.pricingRepository.addPricingCombine(
                PricingCombine.builder()
                        .pricingCombineId(5201003)
                        .pricingPlanId(5100001)
                        .eventPricingStrategyId(5200003)
                        .lifeCycle(
                                LifeCycle.builder()
                                        .state(MainState.AVAILABLE)
                                        .effDate(DateUtils.createDate(2022, 1, 1))
                                        .build())
                        .build()
        );
        this.pricingRepository.addPricingSection(
                PricingSection.builder()
                        .pricingSectionId(5300000)
                        .sectionType(PricingSectionType.NO_CONDITION)
                        .sectionCalcType(SectionCalcType.NO)
                        .pricingSectionName("??????????????????")
                        .pricingRefObjectId(null)
                        .parentSectionId(null)
                        .startRefValue(null)
                        .endRefValue(null)
                        .eventPricingStrategyId(5200001) // ??????????????????????????????
                        .calcPriority(1)
                        .build()
        );
        this.pricingRepository.addPricingSection(
                PricingSection.builder()
                        .pricingSectionId(5300001)
                        .sectionType(PricingSectionType.UNIDIRECTION)
                        .sectionCalcType(SectionCalcType.FORWARD)
                        .pricingSectionName("??????????????????0~7???")
                        .pricingRefObjectId(5001002L) // ?????????.????????????
                        .parentSectionId(5300000L)
                        .startRefValue(
                                RefValue.builder()
                                        .refValueType(RefValueType.CONSTANT)
                                        .valueType(ValueType.TIME)
                                        .valueString("00:00:00")
                                        .ratePrecision(2)
                                        .calcPrecision(2)
                                        .build()
                        )
                        .endRefValue(
                                RefValue.builder()
                                        .refValueType(RefValueType.CONSTANT)
                                        .valueType(ValueType.TIME)
                                        .valueString("06:59:59")
                                        .ratePrecision(2)
                                        .calcPrecision(2)
                                        .build()
                        )
                        .eventPricingStrategyId(5200001) // ??????????????????????????????
                        .calcPriority(1)
                        .build()
        );
        this.pricingRepository.addTariff(
                Tariff.builder()
                        .tariffId(5400001)
                        .tariffType(TariffType.USAGE)
                        .pricingSectionId(5300001)
                        .resource(null)
                        .action(null)
                        .acctITemType(AcctITemType.LONG_DISTANCE_CALL)
                        .tariffUnitId(5000101)
                        .rateUnit(6)
                        .fixedRateValue(
                                RefValue.builder()
                                        .refValueType(RefValueType.CONSTANT)
                                        .valueType(ValueType.DOUBLE)
                                        .valueString("0")
                                        .build()
                        )
                        .scaledRateValue(
                                RefValue.builder()
                                        .refValueType(RefValueType.CONSTANT)
                                        .valueType(ValueType.DOUBLE)
                                        .valueString("0.04")
                                        .build()
                        )
                        .calcPriority(1)
                        .belongCycleDuration(0)
                        .build()
        );
        this.pricingRepository.addPricingSection(
                PricingSection.builder()
                        .pricingSectionId(5300002)
                        .sectionType(PricingSectionType.UNIDIRECTION)
                        .sectionCalcType(SectionCalcType.FORWARD)
                        .pricingSectionName("??????????????????7~24???")
                        .parentSectionId(null)
                        .pricingRefObjectId(5001002L) // ?????????.????????????
                        .parentSectionId(5300000L)
                        .startRefValue(
                                RefValue.builder()
                                        .refValueType(RefValueType.CONSTANT)
                                        .valueType(ValueType.TIME)
                                        .valueString("07:00:00")
                                        .ratePrecision(2)
                                        .calcPrecision(2)
                                        .build()
                        )
                        .endRefValue(
                                RefValue.builder()
                                        .refValueType(RefValueType.CONSTANT)
                                        .valueType(ValueType.TIME)
                                        .valueString("23:59:59")
                                        .ratePrecision(2)
                                        .calcPrecision(2)
                                        .build()
                        )
                        .eventPricingStrategyId(5200001) // ??????????????????????????????
                        .calcPriority(2)
                        .build()
        );
        this.pricingRepository.addTariff(
                Tariff.builder()
                        .tariffId(5400002)
                        .tariffType(TariffType.USAGE)
                        .pricingSectionId(5300002)
                        .resource(null)
                        .action(null)
                        .acctITemType(AcctITemType.LONG_DISTANCE_CALL)
                        .tariffUnitId(5000101)
                        .rateUnit(6)
                        .fixedRateValue(
                                RefValue.builder()
                                        .refValueType(RefValueType.CONSTANT)
                                        .valueType(ValueType.DOUBLE)
                                        .valueString("0")
                                        .build()
                        )
                        .scaledRateValue(
                                RefValue.builder()
                                        .refValueType(RefValueType.CONSTANT)
                                        .valueType(ValueType.DOUBLE)
                                        .valueString("0.07")
                                        .build()
                        )
                        .calcPriority(1)
                        .belongCycleDuration(0)
                        .build()
        );
        this.pricingRepository.addPricingSection(
                PricingSection.builder()
                        .pricingSectionId(5300101)
                        .sectionType(PricingSectionType.NO_CONDITION)
                        .sectionCalcType(SectionCalcType.NO)
                        .pricingSectionName("?????????????????????")
                        .pricingRefObjectId(null)
                        .parentSectionId(null)
                        .startRefValue(null)
                        .endRefValue(null)
                        .eventPricingStrategyId(5200001) // ??????????????????????????????
                        .calcPriority(1)
                        .build()
        );
        this.pricingRepository.addTariff(
                Tariff.builder()
                        .tariffId(5400101)
                        .tariffType(TariffType.USAGE)
                        .pricingSectionId(5300101)
                        .resource(null)
                        .action(null)
                        .acctITemType(AcctITemType.LONG_DISTANCE_CALL_EXTRA)
                        .tariffUnitId(5000101)
                        .rateUnit(6)
                        .fixedRateValue(
                                RefValue.builder()
                                        .refValueType(RefValueType.CONSTANT)
                                        .valueType(ValueType.DOUBLE)
                                        .valueString("0")
                                        .build()
                        )
                        .scaledRateValue(
                                RefValue.builder()
                                        .refValueType(RefValueType.CONSTANT)
                                        .valueType(ValueType.DOUBLE)
                                        .valueString("0.01")
                                        .build()
                        )
                        .calcPriority(1)
                        .belongCycleDuration(0)
                        .build()
        );
        this.pricingRepository.addPricingSection(
                PricingSection.builder()
                        .pricingSectionId(5300003)
                        .sectionType(PricingSectionType.NO_CONDITION)
                        .sectionCalcType(SectionCalcType.FORWARD)
                        .pricingSectionName("??????????????????")
                        .parentSectionId(null)
                        .pricingRefObjectId(null)
                        .eventPricingStrategyId(5200002)
                        .build()
        );
        this.pricingRepository.addTariff(
                Tariff.builder()
                        .tariffId(5400003)
                        .tariffType(TariffType.USAGE)
                        .pricingSectionId(5300003)
                        .resource(null)
                        .action(null)
                        .acctITemType(AcctITemType.LOCAL_CALL)
                        .tariffUnitId(5000101)
                        .rateUnit(6)
                        .fixedRateValue(
                                RefValue.builder()
                                        .refValueType(RefValueType.CONSTANT)
                                        .valueType(ValueType.DOUBLE)
                                        .valueString("0")
                                        .build()
                        )
                        .scaledRateValue(
                                RefValue.builder()
                                        .refValueType(RefValueType.CONSTANT)
                                        .valueType(ValueType.DOUBLE)
                                        .valueString("0.03")
                                        .build()
                        )
                        .calcPriority(1)
                        .belongCycleDuration(0)
                        .build()
        );
        this.pricingRepository.addPricingSection(
                PricingSection.builder()
                        .pricingSectionId(5300004)
                        .sectionType(PricingSectionType.NO_CONDITION)
                        .sectionCalcType(SectionCalcType.FORWARD)
                        .pricingSectionName("???????????????")
                        .parentSectionId(null)
                        .pricingRefObjectId(null)
                        .eventPricingStrategyId(5200003)
                        .build()
        );
        this.pricingRepository.addTariff(
                Tariff.builder()
                        .tariffId(5400004)
                        .tariffType(TariffType.ONCE)
                        .pricingSectionId(5300004)
                        .resource(null)
                        .action(null)
                        .acctITemType(AcctITemType.INITIAL_INSTALLATION)
                        .tariffUnitId(5000102)
                        .rateUnit(6)
                        .fixedRateValue(
                                RefValue.builder()
                                        .refValueType(RefValueType.CONSTANT)
                                        .valueType(ValueType.DOUBLE)
                                        .valueString("120")
                                        .build()
                        )
                        .scaledRateValue(null)
                        .calcPriority(1)
                        .belongCycleDuration(0)
                        .build()
        );
    }
}
