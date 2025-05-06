package ks.glowlyapp.address;

import ks.glowlyapp.address.dto.AddressDto;
import ks.glowlyapp.business.BusinessRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AddressService {
    private final AddressRepository addressRepository;
    private final AddressDtoMapper addressDtoMapper;

    public AddressService(AddressRepository addressRepository, AddressDtoMapper addressDtoMapper) {
        this.addressRepository = addressRepository;
        this.addressDtoMapper = addressDtoMapper;
    }

    public Optional<AddressDto> findAddressByBusiness(long id) {
        return addressRepository.findAddressByBusinessId(id)
                .map(addressDtoMapper::map);
    }

    public Optional<AddressDto> findAddressByCity(String city) {
        return addressRepository.findAddressByCity(city)
                .map(addressDtoMapper::map);
    }

    @Transactional
    public void createNewAddress(AddressDto addressDto){
        Address address = new Address();
        address.setCity(addressDto.getCity());
        address.setPostalCode(addressDto.getPostalCode());
        address.setStreetName(addressDto.getStreetName());
        address.setBuildingNumber(addressDto.getBuildingNumber());
        address.setAdditionalInfo(addressDto.getAdditionalInfo());
        addressRepository.save(address);
    }

    @Transactional
    public void updateAddress(AddressDto addressDto, long addressId) {
        Address addressToUpdate = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        validateAtLeastOneFieldPresent(addressDto);
        applyUpdatesFromDto(addressToUpdate, addressDto);
    }



    @Transactional
    public void deleteAddress(long addressId){
        addressRepository.deleteById(addressId);
    }

    private void validateAtLeastOneFieldPresent(AddressDto dto) {
        if (dto.getCity() == null &&
                dto.getPostalCode() == null &&
                dto.getStreetName() == null &&
                dto.getBuildingNumber() == null &&
                dto.getAdditionalInfo() == null) {
            throw new IllegalArgumentException("At least one field must be provided to update the address");
        }
    }
    private void applyUpdatesFromDto(Address address, AddressDto dto) {
        if (dto.getCity() != null) {
            address.setCity(dto.getCity());
        }
        if (dto.getPostalCode() != null) {
            address.setPostalCode(dto.getPostalCode());
        }
        if (dto.getStreetName() != null) {
            address.setStreetName(dto.getStreetName());
        }
        if (dto.getBuildingNumber() != null) {
            address.setBuildingNumber(dto.getBuildingNumber());
        }
        if (dto.getAdditionalInfo() != null) {
            address.setAdditionalInfo(dto.getAdditionalInfo());
        }
    }


}