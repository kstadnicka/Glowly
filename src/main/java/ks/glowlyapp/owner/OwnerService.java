package ks.glowlyapp.owner;

import ks.glowlyapp.business.Business;
import ks.glowlyapp.business.BusinessRepository;
import ks.glowlyapp.owner.dto.OwnerRegistrationDto;
import ks.glowlyapp.owner.dto.OwnerResponseDto;
import ks.glowlyapp.validation.ValidationUtil;
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
    public void updateOwnerDetails(OwnerRegistrationDto dto, long id) {
        validateOwnerRegistrationDto(dto);
        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        owner.setEmail(dto.getEmail());
        owner.setPhoneNumber(dto.getPhoneNumber());
        owner.setPassword(dto.getPassword());

        List<Business> updateBusinessList = Optional.ofNullable(dto.getBusinessIds())
                .orElse(List.of())
                .stream()
                .map(businessRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        owner.setBusinessList(updateBusinessList);
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
}
