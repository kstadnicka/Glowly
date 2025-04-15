package ks.glowlyapp.owner;

import ks.glowlyapp.business.Business;
import ks.glowlyapp.owner.dto.OwnerRegistrationDto;
import ks.glowlyapp.owner.dto.OwnerResponseDto;
import ks.glowlyapp.user.User;
import ks.glowlyapp.user.dto.UserRegistrationDto;
import ks.glowlyapp.user.dto.UserResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class OwnerService {
    private final OwnerRepository ownerRepository;
    private final OwnerResponseDtoMapper ownerResponseDtoMapper;
    private final OwnerRegistrationDtoMapper ownerRegistrationDtoMapper;


    public OwnerService(OwnerRepository ownerRepository, OwnerResponseDtoMapper ownerResponseDtoMapper, OwnerRegistrationDtoMapper ownerRegistrationDtoMapper) {
        this.ownerRepository = ownerRepository;
        this.ownerResponseDtoMapper = ownerResponseDtoMapper;
        this.ownerRegistrationDtoMapper = ownerRegistrationDtoMapper;
    }

    public Optional<OwnerRegistrationDto> findOwnerByEmail(String email){
        Objects.requireNonNull(email,"Email cannot be null");
        return ownerRepository.findOwnerByEmail(email)
                .map(ownerRegistrationDtoMapper::map);
    }

    public Optional<OwnerResponseDto> findOwnerByLastName(String lastName){
        Objects.requireNonNull(lastName,"Last Name cannot be null");
        return ownerRepository.findOwnerByLastName(lastName)
                .map(ownerResponseDtoMapper::map);
    }

    public List<OwnerResponseDto> getAllOwners(){
        return ownerRepository.findAll()
                .stream()
                .map(ownerResponseDtoMapper::map)
                .toList();
    }

    public Optional<OwnerResponseDto> getOwnerById(long id){
        return ownerRepository.findById(id)
                .map(ownerResponseDtoMapper::map);
    }

    @Transactional
    public void createNewOwner(OwnerRegistrationDto ownerRegistrationDto){
        Owner owner = new Owner();
        owner.setEmail(ownerRegistrationDto.getEmail());
        owner.setPassword(ownerRegistrationDto.getPassword());
        ownerRepository.save(owner);
    }

    @Transactional
    public void updateOwnerDetails(OwnerResponseDto ownerResponseDto, long id){
        Owner ownerToUpdate = ownerRepository.findById(id)
                .orElseGet(Owner::new);
        ownerToUpdate.setFirstName(ownerResponseDto.getFirstName());
        ownerToUpdate.setLastName(ownerResponseDto.getLastName());
        ownerToUpdate.setEmail(ownerResponseDto.getEmail());
        ownerToUpdate.setPhoneNumber(ownerResponseDto.getPhoneNumber());
        List<Business> newBusinessList = ownerResponseDto.getBusinessList();

        if (newBusinessList !=null){
            for (Business newBusiness : newBusinessList) {
                if (!ownerToUpdate.getBusinessList().contains(newBusiness)) {
                    ownerToUpdate.getBusinessList().add(newBusiness);
                }
            }
            ownerToUpdate.getBusinessList().removeIf(existingBusiness ->
                    !newBusinessList.contains(existingBusiness));
        }
        ownerRepository.save(ownerToUpdate);
    }

    @Transactional
    public void deleteOwner(long id){
        ownerRepository.deleteById(id);
    }
}
