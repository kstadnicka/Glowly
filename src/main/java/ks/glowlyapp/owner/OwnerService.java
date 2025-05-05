package ks.glowlyapp.owner;

import ks.glowlyapp.business.Business;
import ks.glowlyapp.business.BusinessRepository;
import ks.glowlyapp.owner.dto.OwnerRegistrationDto;
import ks.glowlyapp.owner.dto.OwnerResponseDto;
import ks.glowlyapp.validation.ValidationUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final BusinessRepository businessRepository;
    private final OwnerResponseDtoMapper ownerResponseDtoMapper;
    private final OwnerRegistrationDtoMapper ownerRegistrationDtoMapper;
    private final ValidationUtil validationUtil;

    public OwnerService(OwnerRepository ownerRepository,
                        BusinessRepository businessRepository,
                        OwnerResponseDtoMapper ownerResponseDtoMapper,
                        OwnerRegistrationDtoMapper ownerRegistrationDtoMapper,
                        ValidationUtil validationUtil) {
        this.ownerRepository = ownerRepository;
        this.businessRepository = businessRepository;
        this.ownerResponseDtoMapper = ownerResponseDtoMapper;
        this.ownerRegistrationDtoMapper = ownerRegistrationDtoMapper;
        this.validationUtil = validationUtil;
    }

    public Optional<OwnerRegistrationDto> findOwnerByEmail(String email) {
        validationUtil.validateNotNull(email, "Email");
        return ownerRepository.findOwnerByEmail(email)
                .map(ownerRegistrationDtoMapper::map);
    }

    public Optional<OwnerResponseDto> findOwnerByLastName(String lastName) {
        validationUtil.validateNotNull(lastName, "Last Name");
        return ownerRepository.findOwnerByLastName(lastName)
                .map(ownerResponseDtoMapper::map);
    }

    public List<OwnerResponseDto> getAllOwners() {
        return ownerRepository.findAll()
                .stream()
                .map(ownerResponseDtoMapper::map)
                .toList();
    }

    public Optional<OwnerResponseDto> getOwnerById(long id) {
        return ownerRepository.findById(id)
                .map(ownerResponseDtoMapper::map);
    }

    @Transactional
    public void createNewOwner(OwnerRegistrationDto dto) {
        validateOwnerRegistrationDto(dto);
        Owner owner = new Owner();
        owner.setEmail(dto.getEmail());
        owner.setPassword(dto.getPassword());
        owner.setPhoneNumber(dto.getPhoneNumber());
        ownerRepository.save(owner);
    }

    @Transactional
    public void updateOwnerDetails(OwnerResponseDto dto, long id) {
        validateOwnerResponseDto(dto);

        Owner ownerToUpdate = ownerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        if (dto.getFirstName() != null) {
            ownerToUpdate.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            ownerToUpdate.setLastName(dto.getLastName());
        }
        if (dto.getEmail() != null) {
            ownerToUpdate.setEmail(dto.getEmail());
        }
        if (dto.getPhoneNumber() != null) {
            ownerToUpdate.setPhoneNumber(dto.getPhoneNumber());
        }
        if (dto.getBusinessList() != null) {
            ownerToUpdate.setBusinessList(dto.getBusinessList());
        }

        ownerRepository.save(ownerToUpdate);
    }
    @Transactional
    public void deleteOwner(long id) {
        ownerRepository.deleteById(id);
    }

    private void validateOwnerRegistrationDto(OwnerRegistrationDto dto) {
        validationUtil.validateNotNull(dto, "OwnerRegistrationDto");
        validationUtil.validateEmailAndPassword(dto.getEmail(), dto.getPassword());
        validationUtil.validatePhoneNumber(dto.getPhoneNumber());
    }

    private void validateOwnerResponseDto(OwnerResponseDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("OwnerResponseDto cannot be null");
        }
        if (dto.getFirstName() == null && dto.getLastName() == null &&
                dto.getEmail() == null && dto.getPhoneNumber() == null &&
                (dto.getBusinessList() == null || dto.getBusinessList().isEmpty())) {
            throw new IllegalArgumentException("At least one field must be provided to update the owner");
        }
    }
}
