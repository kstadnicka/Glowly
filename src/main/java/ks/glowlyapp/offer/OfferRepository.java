package ks.glowlyapp.offer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OfferRepository extends JpaRepository<Offer, Long> {

    Optional<Offer> findOfferByName(String name);
    Optional<Offer> findOfferByPrice(double price);
}
