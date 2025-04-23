package ks.glowlyapp.appointment;

import ks.glowlyapp.appointment.dto.AppointmentDto;
import ks.glowlyapp.business.BusinessRepository;
import ks.glowlyapp.user.User;
import ks.glowlyapp.user.dto.UserResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentDtoMapper appointmentDtoMapper;

    public AppointmentService(AppointmentRepository appointmentRepository, AppointmentDtoMapper appointmentDtoMapper) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentDtoMapper = appointmentDtoMapper;
    }

    public List<AppointmentDto> getAppointmentByUser(long userId){
        return appointmentRepository.findAllByUser_Id(userId)
                .stream()
                .map(appointmentDtoMapper::map)
                .toList();
    }

    public List<AppointmentDto> getAppointmentByBusiness(long businessId){
        return appointmentRepository.findAllByOffer_Business_Id(businessId)
                .stream()
                .map(appointmentDtoMapper::map)
                .toList();
    }

    public List<AppointmentDto> getAppointmentByData(LocalDateTime data){
        validateData(data);
        return appointmentRepository.findAllByData(data)
                .stream()
                .map(appointmentDtoMapper::map)
                .toList();
    }


    private static void validateData(LocalDateTime data) {
        if (data ==null){
            throw new IllegalArgumentException("Appointment date cannot be null");
        }
        if (data.isBefore(LocalDateTime.now().minusYears(1))){
            throw new IllegalArgumentException("Date is too far in the past");
        }
        if (data.isAfter(LocalDateTime.now().plusYears(2))){
            throw new IllegalArgumentException("Data is too far in the future");
        }
    }
}
