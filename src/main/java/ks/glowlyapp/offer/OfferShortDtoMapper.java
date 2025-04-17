package ks.glowlyapp.offer;

import ks.glowlyapp.offer.dto.OfferShortDto;
import org.springframework.stereotype.Component;

@Component
public class OfferShortDtoMapper {

    public OfferShortDto map(Offer offer){
        return new OfferShortDto(
                offer.getId(),
                offer.getName()
        );
    }
}
