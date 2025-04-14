package ks.glowlyapp.user;

import ks.glowlyapp.user.dto.UserResponseDto;

public class UserResponseDtoMapper {

    static UserResponseDto map(User user){
        return new UserResponseDto(
                user.getLastName(),
                user.getFirstName(),
                user.getPassword(),
                user.getPhoneNumber(),
                user.getEmail()
        );
    }
}
