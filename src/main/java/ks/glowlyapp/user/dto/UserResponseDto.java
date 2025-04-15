package ks.glowlyapp.user.dto;

import lombok.Data;

@Data
public class UserResponseDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    public UserResponseDto(String lastName, String firstName, String phoneNumber, String email) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
}
