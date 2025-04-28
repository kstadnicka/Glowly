package ks.glowlyapp.address;

import ks.glowlyapp.address.dto.AddressDto;
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
