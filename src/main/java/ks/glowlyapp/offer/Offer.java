package ks.glowlyapp.offer;

import jakarta.persistence.*;
import ks.glowlyapp.business.Business;
import ks.glowlyapp.owner.Owner;
import lombok.Data;

import java.time.Duration;

@Data
@Entity
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Duration duration;
    private String description;
    private double price;
    @ManyToOne
    private Owner owner;
    @ManyToOne
    private Business business;
}
