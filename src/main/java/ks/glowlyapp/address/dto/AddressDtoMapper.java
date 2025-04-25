package ks.glowlyapp.address.dto;

import ks.glowlyapp.address.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressDtoMapper {
    public AddressDto map(Address address){
        return new AddressDto(
                address.getStreetName(),
                address.getBuildingNumber(),
                address.getCity(),
                address.getPostalCode(),
                address.getAdditionalInfo()
        );
    }
}
