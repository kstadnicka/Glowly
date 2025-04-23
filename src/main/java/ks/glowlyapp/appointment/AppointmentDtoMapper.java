package ks.glowlyapp.appointment;

import ks.glowlyapp.appointment.dto.AppointmentDto;
import org.springframework.stereotype.Component;

@Component
public class AppointmentDtoMapper {
    public AppointmentDto map(Appointment appointment){
        return new AppointmentDto(
                appointment.getData(),
                appointment.getOffer(),
                appointment.getAddress()
        );
    }
}
