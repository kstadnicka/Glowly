package ks.glowlyapp.owner.dto;

import ks.glowlyapp.business.Business;
import lombok.Data;

import java.util.List;

@Data
public class OwnerResponseDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private List <Business> businessList;

    public OwnerResponseDto() {
    }

    public OwnerResponseDto(String firstName, String lastName, String email, String phoneNumber, List<Business> businessList) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.businessList = businessList;
    }
}
