package ks.glowlyapp.offer.dto;

import lombok.Data;

import java.time.Duration;
@Data
public class OfferDto {
    String name;
    String description;
    double price;
    Duration duration;

    public OfferDto(String name, String description, double price, Duration duration) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
    }
}
