package ks.glowlyapp.business;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusinessRepository extends JpaRepository <Business, Long> {

    Optional<Business> findBusinessesByName(String name);
}
