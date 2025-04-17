package ks.glowlyapp.business;

import ks.glowlyapp.business.dto.BusinessDto;
import ks.glowlyapp.offer.dto.OfferShortDto;
import org.springframework.stereotype.Component;

@Component
public class BusinessDtoMapper {

    public BusinessDto map (Business business){
        return new BusinessDto(
                business.getName(),
                business.getAddress(),
                business.getOffersList().stream()
                        .map(offer -> new OfferShortDto(
                                offer.getId(),
                                offer.getName()
                        ))
                        .toList()
        );
    }
}
