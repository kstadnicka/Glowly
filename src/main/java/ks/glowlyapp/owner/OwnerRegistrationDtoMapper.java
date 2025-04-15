package ks.glowlyapp.owner;

import ks.glowlyapp.owner.dto.OwnerRegistrationDto;
import org.springframework.stereotype.Component;

@Component
public class OwnerRegistrationDtoMapper {

    public OwnerRegistrationDto map(Owner owner){
        return new OwnerRegistrationDto(
                owner.getEmail(),
                owner.getPassword()
        );
    }
}
