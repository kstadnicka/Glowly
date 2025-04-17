package ks.glowlyapp.business.dto;

import ks.glowlyapp.address.Address;
import ks.glowlyapp.offer.dto.OfferShortDto;
import lombok.Data;

import java.util.List;

@Data
public class BusinessDto {
    String name;
    Address address;
    List<OfferShortDto> offersList;

    public BusinessDto() {
    }

    public BusinessDto(String name, Address address, List<OfferShortDto> offersList) {
        this.name = name;
        this.address = address;
        this.offersList = offersList;
    }
}
