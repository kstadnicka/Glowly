package ks.glowlyapp.address;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository <Address,Long> {

    Optional<Address> findAddressByBusiness_Id(long id);
    Optional<Address> findAddressByCity(String city);
}