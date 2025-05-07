package ks.glowlyapp.appointment;

import ks.glowlyapp.appointment.dto.AppointmentDto;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/user/{id}/all")
    public ResponseEntity<List<AppointmentDto>> getAllAppointmentsByUsers(@PathVariable long id) {
        List<AppointmentDto> appointments = appointmentService.getAppointmentByUser(id);
        return ResponseEntity.ok(appointments);
    }


    @GetMapping("/business/{id}/all")
    public ResponseEntity<List<AppointmentDto>> getAllAppointmentsByBusiness(@PathVariable long id) {
        List<AppointmentDto> appointments = appointmentService.getAppointmentByBusiness(id);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/available")
    public ResponseEntity<List<LocalTime>> getAvailableSlot(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("addressId") Long addressId
    ) {
        List<LocalTime> availableSlots = appointmentService.getAvailableSlots(date, addressId);
        return ResponseEntity.ok(availableSlots);
    }


    @GetMapping("/user/{id}/today")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsForToday(@PathVariable long userId) {
        List<AppointmentDto> appointments = appointmentService.getAppointmentsForToday(userId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/user/{id}/future")
    public ResponseEntity<List<AppointmentDto>> getFutureAppointments(@PathVariable long id) {
        List<AppointmentDto> futureAppointments = appointmentService.getFutureAppointments(id);
        return ResponseEntity.ok(futureAppointments);
    }

    @PostMapping
    public ResponseEntity<String> createNewAppointment(
            @RequestBody AppointmentDto appointmentDto,
            @RequestParam Long businessId,
            @RequestParam Long userId) {
        appointmentService.createNewAppointment(appointmentDto, userId, businessId);
        return ResponseEntity.ok("Appointment booked successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAppointment(@PathVariable("id") Long appointmentId,
                                                    @PathVariable("id") Long userId,
                                                    @RequestBody AppointmentDto appointmentDto) {
        appointmentService.updateAppointmentDetails(appointmentDto, appointmentId, userId);
        return ResponseEntity.ok("Appointment updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelAppointment(@PathVariable("id") long id) {
        appointmentService.cancelAppointment(id);
        return ResponseEntity.ok("Appointment canceled successfully");
    }
}
