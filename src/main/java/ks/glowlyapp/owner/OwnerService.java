package ks.glowlyapp.owner;

import ks.glowlyapp.business.Business;
import ks.glowlyapp.business.BusinessRepository;
import ks.glowlyapp.owner.dto.OwnerRegistrationDto;
import ks.glowlyapp.owner.dto.OwnerResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class OwnerService {
    private final OwnerRepository ownerRepository;
    private final BusinessRepository businessRepository;
    private final OwnerResponseDtoMapper ownerResponseDtoMapper;
    private final OwnerRegistrationDtoMapper ownerRegistrationDtoMapper;


    public OwnerService(OwnerRepository ownerRepository, BusinessRepository businessRepository, OwnerResponseDtoMapper ownerResponseDtoMapper, OwnerRegistrationDtoMapper ownerRegistrationDtoMapper) {
        this.ownerRepository = ownerRepository;
        this.businessRepository = businessRepository;
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
        owner.setPhoneNumber(ownerRegistrationDto.getPhoneNumber());
        ownerRepository.save(owner);
    }

    @Transactional
    public void updateOwnerDetails(OwnerRegistrationDto ownerRegistrationDto, long id){
      Owner ownerToUpdate = ownerRepository.findById(id)
              .orElseThrow(()-> new RuntimeException("Owner not found"));

      ownerToUpdate.setEmail(ownerRegistrationDto.getEmail());
      ownerToUpdate.setPhoneNumber(ownerRegistrationDto.getPhoneNumber());
      ownerToUpdate.setPassword(ownerRegistrationDto.getPassword());
      List<Business> updateBusinessList = Optional.ofNullable(ownerRegistrationDto.getBusinessIds())
              .orElse(List.of())
              .stream()
              .map(businessRepository::findById)
              .filter(Optional::isPresent)
              .map(Optional::get)
              .toList();
      ownerToUpdate.setBusinessList(updateBusinessList);
    }

    @Transactional
    public void deleteOwner(long id){
        ownerRepository.deleteById(id);
    }
}
