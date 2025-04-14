package ks.glowlyapp.user;

import ks.glowlyapp.user.dto.UserResponseDto;
import org.springframework.stereotype.Component;

@Component
public class UserResponseDtoMapper {

    public UserResponseDto map(User user){
        return new UserResponseDto(
                user.getLastName(),
                user.getFirstName(),
                user.getPassword(),
                user.getPhoneNumber(),
                user.getEmail()
        );
    }
}
