package ks.glowlyapp.owner.dto;

import lombok.Data;

import java.util.List;

@Data
public class OwnerRegistrationDto {

    private String email;
    private String password;
    private String phoneNumber;
    private List<Long> businessIds;

    public OwnerRegistrationDto() {
    }

    public OwnerRegistrationDto(String email, String password,String phoneNumber, List<Long> businessIds) {
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.businessIds = businessIds;
    }
}
