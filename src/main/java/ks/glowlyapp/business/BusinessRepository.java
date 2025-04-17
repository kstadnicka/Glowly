package ks.glowlyapp.business;

import ks.glowlyapp.business.dto.BusinessDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusinessRepository extends JpaRepository <Business, Long> {

    Optional<BusinessDto> findBusinessesByName(String name);
}
