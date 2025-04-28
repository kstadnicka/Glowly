package ks.glowlyapp.user;


import ks.glowlyapp.user.dto.UserRegistrationDto;
import ks.glowlyapp.user.dto.UserResponseDto;
import ks.glowlyapp.validation.ValidationUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserRegistrationDtoMapper userRegistrationDtoMapper;
    private final UserResponseDtoMapper userResponseDtoMapper;
    private final ValidationUtil validationUtil;

    public UserService(UserRepository userRepository,
                       UserRegistrationDtoMapper userRegistrationDtoMapper,
                       UserResponseDtoMapper userResponseDtoMapper,
                       ValidationUtil validationUtil) {
        this.userRepository = userRepository;
        this.userRegistrationDtoMapper = userRegistrationDtoMapper;
        this.userResponseDtoMapper = userResponseDtoMapper;
        this.validationUtil = validationUtil;
    }

    public Optional<UserRegistrationDto> findUserByEmail(String email) {
        validationUtil.validateNotNull(email, "Email");
        return userRepository.findUserByEmail(email)
                .map(userRegistrationDtoMapper::map);
    }

    public Optional<UserResponseDto> findUserByLastName(String lastName) {
        validationUtil.validateNotNull(lastName, "Last name");
        return userRepository.findUserByLastName(lastName)
                .map(userResponseDtoMapper::map);
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userResponseDtoMapper::map)
                .toList();
    }

    public Optional<UserResponseDto> getUserById(long id) {
        return userRepository.findById(id)
                .map(userResponseDtoMapper::map);
    }

    @Transactional
    public void createNewUser(UserRegistrationDto dto) {
        validateUserRegistrationDto(dto);
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        userRepository.save(user);
    }

    @Transactional
    public void updateUserDetails(UserResponseDto dto, long id) {
        validateUserResponseDto(dto);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (dto.getFirstName() != null) {
            user.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            user.setLastName(dto.getLastName());
        }
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getPhoneNumber() != null) {
            user.setPhoneNumber(dto.getPhoneNumber());
        }

        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    private void validateUserRegistrationDto(UserRegistrationDto dto) {
        validationUtil.validateNotNull(dto, "UserRegistrationDto");
        validationUtil.validateEmailAndPassword(dto.getEmail(), dto.getPassword());
    }

    private void validateUserResponseDto(UserResponseDto dto) {
        validationUtil.validateNotNull(dto, "UserResponseDto");

        if (dto.getFirstName() != null) {
            validationUtil.validateStringNotEmpty(dto.getFirstName(), "First name");
        }

        if (dto.getLastName() != null) {
            validationUtil.validateStringNotEmpty(dto.getLastName(), "Last name");
        }

        if (dto.getEmail() != null) {
            validationUtil.validateStringNotEmpty(dto.getEmail(), "Email");
        }
        if (dto.getPhoneNumber() != null) {
            validationUtil.validatePhoneNumber(dto.getPhoneNumber());
        }
    }
}
