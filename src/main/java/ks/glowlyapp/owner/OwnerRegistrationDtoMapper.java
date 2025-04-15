package ks.glowlyapp.owner;

import ks.glowlyapp.business.Business;
import ks.glowlyapp.owner.dto.OwnerRegistrationDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class OwnerRegistrationDtoMapper {

    public OwnerRegistrationDto map(Owner owner){
        List<Long> businessIds = Optional.ofNullable(owner.getBusinessList())
                .orElse(List.of())
                .stream()
                .map(Business::getId)
                .toList();

        return new OwnerRegistrationDto(
                owner.getEmail(),
                owner.getPassword(),
                owner.getPhoneNumber(),
                businessIds
        );
    }
}
