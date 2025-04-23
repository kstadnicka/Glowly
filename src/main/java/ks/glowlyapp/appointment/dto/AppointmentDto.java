package ks.glowlyapp.appointment.dto;

import ks.glowlyapp.address.Address;
import ks.glowlyapp.offer.Offer;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class AppointmentDto {
    LocalDateTime appointmentData;
    Offer offer;
    Address address;

    public AppointmentDto() {
    }

    public AppointmentDto(LocalDateTime appointmentData, Offer offer, Address address) {
        this.appointmentData = appointmentData;
        this.offer = offer;
        this.address = address;
    }
}
