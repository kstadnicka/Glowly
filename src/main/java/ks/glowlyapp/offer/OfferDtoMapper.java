package ks.glowlyapp.offer;

import ks.glowlyapp.offer.dto.OfferDto;
import org.springframework.stereotype.Component;

@Component
public class OfferDtoMapper {

    public OfferDto map(Offer offer){
        return new OfferDto(
                offer.getName(),
                offer.getDescription(),
                offer.getPrice(),
                offer.getDuration()
        );
    }
}
