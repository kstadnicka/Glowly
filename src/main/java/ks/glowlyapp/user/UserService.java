package ks.glowlyapp.user;


import ks.glowlyapp.user.dto.UserRegistrationDto;
import ks.glowlyapp.user.dto.UserResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserRegistrationDtoMapper userRegistrationDtoMapper;
    private final UserResponseDtoMapper userResponseDtoMapper;

    public UserService(UserRepository userRepository,
                       UserRegistrationDtoMapper userRegistrationDtoMapper,
                       UserResponseDtoMapper userResponseDtoMapper) {
        this.userRepository = userRepository;
        this.userRegistrationDtoMapper = userRegistrationDtoMapper;
        this.userResponseDtoMapper = userResponseDtoMapper;
    }

    public Optional<UserRegistrationDto> findUserByEmail(String email) {
        validateNotNull(email, "Email");
        return userRepository.findUserByEmail(email)
                .map(userRegistrationDtoMapper::map);
    }

    public Optional<UserResponseDto> findUserByLastName(String lastName) {
        validateNotNull(lastName, "Last name");
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
    public void createNewUser(UserRegistrationDto userRegistrationDto) {
        validateRegistrationDto(userRegistrationDto);
        User user = new User();
        user.setEmail(userRegistrationDto.getEmail());
        user.setPassword(userRegistrationDto.getPassword());
        userRepository.save(user);
    }

    @Transactional
    public void updateUserDetails(UserResponseDto userResponseDto, long id) {
        validateResponseDto(userResponseDto);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(userResponseDto.getFirstName());
        user.setLastName(userResponseDto.getLastName());
        user.setEmail(userResponseDto.getEmail());
        user.setPhoneNumber(userResponseDto.getPhoneNumber());
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    private void validateNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }
    }

    private void validateRegistrationDto(UserRegistrationDto dto) {
        if (dto == null) throw new IllegalArgumentException("UserRegistrationDto cannot be null");
        validateNotNull(dto.getEmail(), "Email");
        validateNotNull(dto.getPassword(), "Password");
    }

    private void validateResponseDto(UserResponseDto dto) {
        if (dto == null) throw new IllegalArgumentException("UserResponseDto cannot be null");
        validateNotNull(dto.getFirstName(), "First name");
        validateNotNull(dto.getLastName(), "Last name");
        validateNotNull(dto.getEmail(), "Email");
        validateNotNull(dto.getPhoneNumber(), "Phone number");
    }
}
