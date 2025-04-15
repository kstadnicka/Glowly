package ks.glowlyapp.offer.dto;

import lombok.Data;

@Data
public class OfferShortDto {
    String name;

    public OfferShortDto(String name) {
        this.name = name;
    }
}
