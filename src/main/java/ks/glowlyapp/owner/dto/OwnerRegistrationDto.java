package ks.glowlyapp.owner.dto;

import lombok.Data;

@Data
public class OwnerRegistrationDto {

    private String email;
    private String password;

    public OwnerRegistrationDto() {
    }

    public OwnerRegistrationDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
