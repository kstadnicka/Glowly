package ks.glowlyapp.user.dto;

import lombok.Data;

@Data
public class UserResponseDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;

    public UserResponseDto(String lastName, String firstName, String password, String phoneNumber, String email) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
}
