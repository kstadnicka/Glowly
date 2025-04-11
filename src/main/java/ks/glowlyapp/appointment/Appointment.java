package ks.glowlyapp.appointment;


import jakarta.persistence.*;
import ks.glowlyapp.address.Address;
import ks.glowlyapp.offer.Offer;
import ks.glowlyapp.user.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    User user;
    LocalDateTime appointmentData;
    @ManyToOne
    Offer offer;
    @ManyToOne
    Address address;
}
