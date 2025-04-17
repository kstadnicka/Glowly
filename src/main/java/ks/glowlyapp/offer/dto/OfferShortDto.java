package ks.glowlyapp.offer.dto;

import lombok.Data;

@Data
public class OfferShortDto {
    Long id;
    String name;

    public OfferShortDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
