package ks.glowlyapp.user;

import ks.glowlyapp.user.dto.UserRegistrationDto;

public class UserRegistrationDtoMapper {

    UserRegistrationDto map(User user){
        return new UserRegistrationDto(
                user.getEmail(),
                user.getPassword()
        );
    }
}
