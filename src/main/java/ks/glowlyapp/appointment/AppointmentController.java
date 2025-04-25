package ks.glowlyapp.appointment;

import ks.glowlyapp.appointment.dto.AppointmentDto;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class AppointmentController {
    public static final String NOTIFICATION_ATTRIBUTE = "notification";

    AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/appointments/user/{id}/all")
    public String getAllAppointmentsByUsers(@PathVariable long id, Model model) {
        List<AppointmentDto> appointments = appointmentService.getAppointmentByUser(id);
        model.addAttribute("appointments", appointments);
        return "appointments-list";
    }


    @GetMapping("/appointments/business/{id}")
    public String getAllAppointmentsByBusiness(@PathVariable long id, Model model) {
        List<AppointmentDto> appointments = appointmentService.getAppointmentByBusiness(id);
        model.addAttribute("appointments", appointments);
        return "appointments-list";
    }

    @GetMapping("/appointments/available")
    public String getAvailableSlot(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("addressId") Long addressId,
            Model model
    ) {
        List<LocalTime> availableSlots = appointmentService.getAvailableSlots(date, addressId);
        model.addAttribute("availableSlots", availableSlots);
        return "available-slots";
    }


    @GetMapping("/appointments/user/{id}/today")
    public String getAppointmentsForToday(@PathVariable long userId, Model model) {
        List<AppointmentDto> appointments = appointmentService.getAppointmentsForToday(userId);
        model.addAttribute("appointments", appointments);
        return "appointments-list";
    }

    @GetMapping("/appointments/user/{id}/future")
    public String getFutureAppointments(@PathVariable long id, Model model) {
        List<AppointmentDto> futureAppointments = appointmentService.getFutureAppointments(id);
        model.addAttribute("appointments", futureAppointments);
        return "appointments-list";
    }


    @PostMapping("/appointments/new")
    public String createNewAppointment(
            @ModelAttribute AppointmentDto appointmentDto,
            @RequestParam Long businessId,
            @RequestParam Long userId,
            RedirectAttributes redirectAttributes) {
        appointmentService.createNewAppointment(appointmentDto, userId, businessId);
        redirectAttributes.addFlashAttribute(NOTIFICATION_ATTRIBUTE, "Wizyta umiówiona!");
        return "redirect:/";
    }

    @GetMapping("/appointments/new")
    public String newAppointmentForm(Model model) {
        AppointmentDto appointment = new AppointmentDto();
        model.addAttribute("appointment", appointment);
        return "appointment-form";
    }

    @PostMapping("/appointments/update")
    public String updateAppointment(@RequestParam Long appointmentId,
                                     @RequestParam Long userId,
                                     @ModelAttribute AppointmentDto appointmentDto,
                                     RedirectAttributes redirectAttributes) {
        appointmentService.updateAppointmentDetails(appointmentDto, appointmentId, userId);
        redirectAttributes.addFlashAttribute(NOTIFICATION_ATTRIBUTE, "Pomyślnie zaktualizowano informację o wizycie");
        return "redirect:/";
    }

    @PostMapping("/appointments/cancel")
    public String cancelAppointment(long appointmentId, RedirectAttributes redirectAttributes) {
        appointmentService.cancelAppointment(appointmentId);
        redirectAttributes.addFlashAttribute(NOTIFICATION_ATTRIBUTE, "Wizyta odwołana");
        return "redirect:/";
    }
}
