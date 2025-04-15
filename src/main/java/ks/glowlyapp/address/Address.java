package ks.glowlyapp.address;

import jakarta.persistence.*;
import ks.glowlyapp.business.Business;
import lombok.Data;

@Data
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String streetName;
    private String buildingNumber;
    private String city;
    private String postalCode;
    private String additionalInfo;
    @OneToOne(mappedBy = "address")
    private Business business;
}
