package ks.glowlyapp.business;

import jakarta.persistence.*;
import ks.glowlyapp.address.Address;
import ks.glowlyapp.offer.Offer;
import ks.glowlyapp.owner.Owner;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne
    private Owner owner;
    @OneToOne
    Address address;
    @OneToMany(mappedBy = "business")
    private List<Offer> offersList = new ArrayList<>();

}
