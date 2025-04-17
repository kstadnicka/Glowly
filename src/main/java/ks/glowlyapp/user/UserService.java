package ks.glowlyapp.user;


import ks.glowlyapp.user.dto.UserRegistrationDto;
import ks.glowlyapp.user.dto.UserResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserRegistrationDtoMapper userRegistrationDtoMapper;
    private final UserResponseDtoMapper userResponseDtoMapper;

    public UserService(UserRepository userRepository, UserRegistrationDtoMapper userRegistrationDtoMapper, UserResponseDtoMapper userResponseDtoMapper) {
        this.userRepository = userRepository;
        this.userRegistrationDtoMapper = userRegistrationDtoMapper;
        this.userResponseDtoMapper = userResponseDtoMapper;
    }

    public Optional<UserRegistrationDto> findUserByEmail(String email){
        Objects.requireNonNull(email,"Email cannot be null");
        return userRepository.findUserByEmail(email)
                .map(userRegistrationDtoMapper::map);
    }

    public Optional<UserResponseDto> findUserByLastName (String lastName){
        Objects.requireNonNull(lastName,"Last name cannot be null");

        return userRepository.findUserByLastName(lastName)
                .map(userResponseDtoMapper::map);
    }

    public List<UserResponseDto> getAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(userResponseDtoMapper::map)
                .toList();
    }

    public Optional<UserResponseDto> getUserById(long id){
        return userRepository.findById(id)
                .map(userResponseDtoMapper::map);
    }

    @Transactional
    public void createNewUser(UserRegistrationDto userRegistrationDto){
        User user = new User();
        user.setEmail(userRegistrationDto.getEmail());
        user.setPassword(userRegistrationDto.getPassword());
        userRepository.save(user);
    }

    @Transactional
    public void updateUserDetails(UserResponseDto userResponseDto,long id){
        User userToUpdate = userRepository.findById(id)
                .orElseThrow(()->new RuntimeException("User not found."));
        userToUpdate.setFirstName(userResponseDto.getFirstName());
        userToUpdate.setLastName(userResponseDto.getLastName());
        userToUpdate.setEmail(userResponseDto.getEmail());
        userToUpdate.setPhoneNumber(userResponseDto.getPhoneNumber());
        userRepository.save(userToUpdate);
    }

    @Transactional
    public void deleteUser(long id){
        userRepository.deleteById(id);
    }
}
