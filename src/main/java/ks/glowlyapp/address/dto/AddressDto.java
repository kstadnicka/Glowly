package ks.glowlyapp.address.dto;

import lombok.Data;

@Data
public class AddressDto {
    private String streetName;
    private String buildingNumber;
    private String city;
    private String postalCode;
    private String additionalInfo;

    public AddressDto() {
    }

    public AddressDto(String streetName, String buildingNumber, String city, String postalCode, String additionalInfo) {
        this.streetName = streetName;
        this.buildingNumber = buildingNumber;
        this.city = city;
        this.postalCode = postalCode;
        this.additionalInfo = additionalInfo;
    }
}
