package ks.glowlyapp.user;

import ks.glowlyapp.user.dto.UserRegistrationDto;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationDtoMapper {

    public UserRegistrationDto map(User user){
        return new UserRegistrationDto(
                user.getEmail(),
                user.getPassword()
        );
    }
}
