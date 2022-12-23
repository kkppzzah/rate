package com.ppzzl.learn.billling.repository;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.ppzzl.learn.billling.model.pricing.*;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PricingRepository {
    private final Map<Long, PricingPlan> pricingPlans = new HashMap<>();
    private final Map<Long, EventPricingStrategy> eventPricingStrategies = new HashMap<>();
    private final Multimap<Long, PricingSection> eventPricingStrategySections = ArrayListMultimap.create(); // key: EventPricingStrategy.id
    private final Map<Long, PricingSection> pricingSections = new HashMap<>(); // key: PricingSection.id
    private final Multimap<Long, PricingSection> childPricingSections = ArrayListMultimap.create(); // key: PricingSection.ParentId
    private final Map<Long, Owner> owners = new HashMap<>();
    private final Map<Long, PricingRefObject> pricingRefObjects = new HashMap<>();
    private final Map<Long, Tariff> tariffs = new HashMap<>();
    private final Multimap<Long, Tariff> pricingSectionTariffs = ArrayListMultimap.create(); // key: PricingSection.id
    private final Map<Long, PricingCombine> pricingCombines = new HashMap<>(); // key: PricingCombine.id
    private final Multimap<Long, PricingCombine> pricingPlanCombines = ArrayListMultimap.create(); // key: PricingPlan.id
    private final Map<Long, TariffUnit> tariffUnits = new HashMap<>(); // TariffUnit.id

    public void addPricingPlan(PricingPlan pricingPlan) {
        this.pricingPlans.put(pricingPlan.getPricingPlanId(), pricingPlan);
    }

    public Optional<PricingPlan> getPricingPlan(long pricingPlanId) {
        return Optional.of(this.pricingPlans.get(pricingPlanId));
    }

    public void addEventPricingStrategy(EventPricingStrategy eventPricingStrategy) {
        this.eventPricingStrategies.put(eventPricingStrategy.getEventPricingStrategyId(), eventPricingStrategy);
    }

    public Optional<EventPricingStrategy> getEventPricingStrategy(long eventPricingStrategyId) {
        return Optional.of(this.eventPricingStrategies.get(eventPricingStrategyId));
    }

    public void addPricingSection(PricingSection pricingSection) {
        this.eventPricingStrategySections.put(pricingSection.getEventPricingStrategyId(), pricingSection);
        this.pricingSections.put(pricingSection.getPricingSectionId(), pricingSection);
        if (pricingSection.getParentSectionId() != null) {
            this.childPricingSections.put(pricingSection.getParentSectionId(), pricingSection);
        }
    }

    public Optional<PricingSection> getPricingSection(long pricingSectionId) {
        return Optional.of(this.pricingSections.get(pricingSectionId));
    }

    public Optional<Collection<PricingSection>> getChildPricingSections(long pricingSectionId) {
        return Optional.of(this.childPricingSections.get(pricingSectionId));
    }

    public Optional<Collection<PricingSection>> getEventPricingStrategySections(long eventPricingStrategyId) {
        return Optional.of(this.eventPricingStrategySections.get(eventPricingStrategyId));
    }

    public void addOwner(Owner owner) {
        this.owners.put(owner.getOwnerId(), owner);
    }

    public Optional<Owner> getOwner(long ownerId) {
        return Optional.of(this.owners.get(ownerId));
    }

    public void addPricingRefObject(PricingRefObject pricingRefObject) {
        this.pricingRefObjects.put(pricingRefObject.getPricingRefObjectId(), pricingRefObject);
    }

    public Optional<PricingRefObject> getPricingRefObject(long pricingRefObjectId) {
        return Optional.of(this.pricingRefObjects.get(pricingRefObjectId));
    }

    public void addTariff(Tariff tariff) {
        this.tariffs.put(tariff.getTariffId(), tariff);
        this.pricingSectionTariffs.put(tariff.getPricingSectionId(), tariff);
    }

    public Optional<Tariff> getTariff(long tariffId) {
        return Optional.of(this.tariffs.get(tariffId));
    }

    public Optional<Collection<Tariff>> getPricingSectionTariffs(long pricingSectionId) {
        return Optional.of(this.pricingSectionTariffs.get(pricingSectionId));
    }

    public void addPricingCombine(PricingCombine pricingCombine) {
        this.pricingCombines.put(pricingCombine.getPricingCombineId(), pricingCombine);
        this.pricingPlanCombines.put(pricingCombine.getPricingPlanId(), pricingCombine);
    }

    public Collection<PricingCombine> findPricingCombineByPlanAndEvent(long pricingPlanId, long eventTypeId) {
        Collection<PricingCombine> tmpPricingCombines = this.pricingPlanCombines.get(pricingPlanId);
        return tmpPricingCombines.stream()
                .filter(pricingCombine ->
                        this.getEventPricingStrategy(pricingCombine.getEventPricingStrategyId())
                                .get().getEventTypeId() == eventTypeId)
                .collect(Collectors.toList());
    }

    public void addTariffUnit(TariffUnit tariffUnit) {
        this.tariffUnits.put(tariffUnit.getTariffUnitId(), tariffUnit);
    }

    public Optional<TariffUnit> getTariffUnit(long tariffUnitId) {
        return Optional.of(this.tariffUnits.get(tariffUnitId));
    }
}
