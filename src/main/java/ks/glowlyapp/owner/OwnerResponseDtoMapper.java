package ks.glowlyapp.owner;

import ks.glowlyapp.owner.dto.OwnerResponseDto;
import org.springframework.stereotype.Component;

@Component
public class OwnerResponseDtoMapper {

    public OwnerResponseDto map(Owner owner){
        return new OwnerResponseDto(
                owner.getFirstName(),
                owner.getLastName(),
                owner.getEmail(),
                owner.getPhoneNumber(),
                owner.getBusinessList()
        );
    }
}
