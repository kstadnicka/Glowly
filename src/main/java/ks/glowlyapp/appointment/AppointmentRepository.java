package ks.glowlyapp.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findAllByUser_Id(Long userId);
    List<Appointment> findAllByOffer_Business_Id(Long businessId);
    List<Appointment> findAllByData(LocalDateTime data);
    boolean existsByUserIdAndData(Long userId, LocalDateTime dateTime);
}
