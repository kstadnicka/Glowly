package ks.glowlyapp.owner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository <Owner,Long> {

    Optional<Owner> findOwnerByEmail(String email);
    Optional<Owner> findOwnerByLastName(String lastName);
}
