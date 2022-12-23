package com.ppzzl.learn.billling.rate;

import com.ppzzl.learn.billling.model.common.ObjectType;
import com.ppzzl.learn.billling.model.event.EventAttr;
import com.ppzzl.learn.billling.model.event.EventContent;
import com.ppzzl.learn.billling.model.pricing.Owner;
import com.ppzzl.learn.billling.model.pricing.PricingRefObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ppzzl.learn.billling.repository.PricingRepository;

@Component
public class PricingRefObjectResolver {
    private final PricingRepository pricingRepository;

    @Autowired
    public PricingRefObjectResolver(PricingRepository pricingRepository) {
        this.pricingRepository = pricingRepository;
    }

    public Object resolve(PricingRefObject pricingRefObject, Context context) {
        Owner owner = this.pricingRepository.getOwner(pricingRefObject.getOwnerId()).get();
        if (owner.getOwnerObjectType().equals(ObjectType.EVENT)) {
            EventContent eventContent =
                    context.getEvent().getEventContent(EventAttr.from(pricingRefObject.getPropertyDefineId()));
            if (eventContent != null) {
                return eventContent.getValue();
            }
        }
        return null;
    }
}
