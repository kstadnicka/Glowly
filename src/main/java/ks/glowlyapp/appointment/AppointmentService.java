package ks.glowlyapp.appointment;

import ks.glowlyapp.address.Address;
import ks.glowlyapp.address.AddressRepository;
import ks.glowlyapp.appointment.dto.AppointmentDto;
import ks.glowlyapp.offer.Offer;
import ks.glowlyapp.offer.OfferRepository;
import ks.glowlyapp.user.User;
import ks.glowlyapp.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final OfferRepository offerRepository;
    private final AddressRepository addressRepository;
    private final AppointmentDtoMapper appointmentDtoMapper;

    public AppointmentService(AppointmentRepository appointmentRepository, UserRepository userRepository, OfferRepository offerRepository, AddressRepository addressRepository, AppointmentDtoMapper appointmentDtoMapper) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.offerRepository = offerRepository;
        this.addressRepository = addressRepository;
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


    public boolean isSlotAvailable(LocalDateTime dateTime, long addressId){
        List<Appointment> appointments = appointmentRepository.findAllByAddressId(addressId);
        return appointments.stream()
                .noneMatch(appointment -> appointment.getData().equals(dateTime));
    }

    public List<LocalTime> getAvailableSlots(LocalDate date, Long addressId) {
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(17, 0);

        List<Appointment> appointments = appointmentRepository.findAllByAddressId(addressId)
                .stream()
                .filter(a -> a.getData().toLocalDate().equals(date))
                .toList();

        List<LocalTime> bookedTimes = appointments.stream()
                .map(a -> a.getData().toLocalTime())
                .toList();

        List<LocalTime> availableSlots = new ArrayList<>();
        LocalTime current = start;

        while (current.isBefore(end)) {
            if (!bookedTimes.contains(current)) {
                availableSlots.add(current);
            }
            current = current.plusMinutes(50);
        }
        return availableSlots;
    }

    public List<AppointmentDto> getAppointmentsForToday(long userId) {
        LocalDate today = LocalDate.now();
        return appointmentRepository.findAllByUser_Id(userId).stream()
                .filter(appointment -> appointment.getData().toLocalDate().equals(today))
                .map(appointmentDtoMapper::map)
                .toList();
    }

    public List<AppointmentDto> getFutureAppointments(long userId) {
        getUserById(userId);
        LocalDateTime now = LocalDateTime.now();
        return appointmentRepository.findAllByUser_Id(userId).stream()
                .filter(appointment -> appointment.getData().isAfter(now))
                .map(appointmentDtoMapper::map)
                .toList();
    }

    @Transactional
    public void createNewAppointment(AppointmentDto appointmentDto, Long userId, Long businessId) {
        User user = getUserById(userId);
        Offer offer = getOfferByIdAndValidateBusiness(appointmentDto.getOffer().getId(), businessId);
        Address address = getAddressById(appointmentDto.getAddress().getId());
        validateUserAvailability(appointmentDto, userId);
        validateAppointmentDto(appointmentDto);

        Appointment appointment = new Appointment();
        appointment.setData(appointmentDto.getAppointmentData());
        appointment.setOffer(offer);
        appointment.setAddress(address);
        appointment.setUser(user);

        appointmentRepository.save(appointment);
    }

    @Transactional
    public void updateAppointmentDetails(AppointmentDto appointmentDto, long appointmentId, long userId){
        Appointment appointmentToUpdate = appointmentRepository.findById(appointmentId)
                .orElseThrow(()->new RuntimeException("Appointment not found"));
        validateAppointmentDto(appointmentDto);
        validateUserAvailability(appointmentDto,userId);
        validateSlotAvailability(appointmentDto);

        appointmentToUpdate.setData(appointmentDto.getAppointmentData());
        appointmentToUpdate.setOffer(appointmentDto.getOffer());
        appointmentToUpdate.setAddress(appointmentDto.getAddress());
    }


    @Transactional
    public void cancelAppointment(long appointmentId){
        appointmentRepository.deleteById(appointmentId);
    }


    private void validateAppointmentDto(AppointmentDto dto) {
        if (dto == null) throw new IllegalArgumentException("Appointment data cannot be null");
        if (dto.getAppointmentData() == null) throw new IllegalArgumentException("Appointment date cannot be null");
        if (dto.getAddress() == null || dto.getAddress().getId() == null)
            throw new IllegalArgumentException("Address or address ID cannot be null");
        if (dto.getOffer() == null || dto.getOffer().getId() == null)
            throw new IllegalArgumentException("Offer or offer ID cannot be null");
    }

    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private Offer getOfferByIdAndValidateBusiness(Long offerId, Long businessId) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Offer not found"));
        if (!offer.getBusiness().getId().equals(businessId)) {
            throw new RuntimeException("This offer does not belong to the specified business");
        }
        return offer;
    }

    private Address getAddressById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));
    }
    
    private void validateUserAvailability(AppointmentDto appointmentDto, Long userId) {
        if (appointmentRepository.existsByUserIdAndData(userId, appointmentDto.getAppointmentData())) {
            throw new RuntimeException("User already has an appointment at this time");
        }
    }

    private void validateSlotAvailability(AppointmentDto appointmentDto) {
        if (!isSlotAvailable(appointmentDto.getAppointmentData(), appointmentDto.getAddress().getId())) {
            throw new RuntimeException("This time slot is already taken");
        }
    }

}
