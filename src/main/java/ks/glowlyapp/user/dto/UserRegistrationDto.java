package ks.glowlyapp.user.dto;

import lombok.Data;

@Data
public class UserRegistrationDto {
    private String email;
    private String password;

    public UserRegistrationDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
