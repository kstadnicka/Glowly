package ks.glowlyapp.user;

import ks.glowlyapp.user.dto.UserRegistrationDto;
import ks.glowlyapp.user.dto.UserResponseDto;
import ks.glowlyapp.validation.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRegistrationDtoMapper userRegistrationDtoMapper;

    @Mock
    private UserResponseDtoMapper userResponseDtoMapper;

    @Mock
    private ValidationUtil validationUtil;

    @InjectMocks
    private UserService userService;

    private final long id = 1L;
    private final String email = "test@example.com";
    private final String password = "testPass";
    private User user;
    private UserRegistrationDto userRegistrationDto;
    private UserResponseDto userResponseDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail(email);
        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail(email);
        userRegistrationDto.setPassword(password);
        userResponseDto = new UserResponseDto();
        userResponseDto.setEmail(email);

    }

    @Test
    void shouldReturnUserRegistrationDtoWhenUserExist() {
        //given
        doNothing().when(validationUtil).validateNotNull(email, "Email");
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
        when(userRegistrationDtoMapper.map(user)).thenReturn(userRegistrationDto);

        //when
        Optional<UserRegistrationDto> result = userService.findUserByEmail(email);

        //then
        assertTrue(result.isPresent());
        assertEquals(userRegistrationDto, result.get());

        verify(validationUtil).validateNotNull(email, "Email");
        verify(userRepository).findUserByEmail(email);
        verify(userRegistrationDtoMapper).map(user);
    }

    @Test
    void shouldReturnEmptyOptionalWhenUserIsNotFound() {
        //given
        doNothing().when(validationUtil).validateNotNull(email, "Email");
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());

        //when
        Optional<UserRegistrationDto> result = userService.findUserByEmail(email);

        //then
        assertTrue(result.isEmpty());

        verify(validationUtil).validateNotNull(email, "Email");
        verify(userRepository).findUserByEmail(email);
        verify(userRegistrationDtoMapper, never()).map(any());
    }

    @Test
    void shouldThrowExceptionInFindUserByEmailWhenEmailIsNull() {
        String nullEmail = null;

        //given
        doThrow(new IllegalArgumentException("Email cannot be null")).when(validationUtil).validateNotNull(nullEmail, "Email");

        //then when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.findUserByEmail(nullEmail));

        assertEquals("Email cannot be null", exception.getMessage());

        verify(validationUtil).validateNotNull(nullEmail, "Email");
        verifyNoMoreInteractions(userRepository, userRegistrationDtoMapper);
    }

    @Test
    void shouldReturnAllUsersWhenExist() {

        //given
        User user2 = new User();
        user2.setEmail("another@example.com");

        UserResponseDto userResponseDto2 = new UserResponseDto();
        userResponseDto2.setEmail("another@example.com");
        List<User> users = List.of(user, user2);
        when(userRepository.findAll()).thenReturn(users);
        when(userResponseDtoMapper.map(user)).thenReturn(userResponseDto);
        when(userResponseDtoMapper.map(user2)).thenReturn(userResponseDto2);

        //when
        List<UserResponseDto> allUsers = userService.getAllUsers();

        //then
        assertEquals(2, allUsers.size());
        assertEquals(userResponseDto, allUsers.get(0));
        assertEquals(userResponseDto2, allUsers.get(1));

        verify(userRepository).findAll();
        verify(userResponseDtoMapper).map(user);
        verify(userResponseDtoMapper).map(user2);
    }

    @Test
    void shouldReturnEmptyListWhenUsersNotExist() {

        //given
        List<User> users = List.of();
        when(userRepository.findAll()).thenReturn(users);

        //when
        List<UserResponseDto> allUsers = userService.getAllUsers();

        //then
        assertEquals(0, allUsers.size());
        verify(userRepository).findAll();

    }

    @Test
    void shouldReturnUserByIdWhenExist() {
        //given
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userResponseDtoMapper.map(user)).thenReturn(userResponseDto);

        //when
        Optional<UserResponseDto> result = userService.getUserById(id);

        //then
        assertTrue(result.isPresent());
        assertEquals(userResponseDto, result.get());

        verify(userRepository).findById(id);
        verify(userResponseDtoMapper).map(user);
    }

    @Test
    void shouldReturnEmptyOptionalWhenUserNotExist() {
        //given
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        //when
        Optional<UserResponseDto> result = userService.getUserById(id);

        //then
        assertTrue(result.isEmpty());

        verify(userRepository).findById(id);
        verify(userResponseDtoMapper, never()).map(any());
    }

    @Test
    void shouldSaveUserWhenRegistrationDtoIsValid() {
        //given
        doNothing().when(validationUtil).validateNotNull(userRegistrationDto, "UserRegistrationDto");
        doNothing().when(validationUtil).validateEmailAndPassword(email, password);

        //when
        userService.createNewUser(userRegistrationDto);

        //then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals(email, savedUser.getEmail());
        assertEquals(password,savedUser.getPassword());

        verify(validationUtil).validateNotNull(userRegistrationDto, "UserRegistrationDto");
        verify(validationUtil).validateEmailAndPassword(email,password);
    }

    @Test
    void shouldThrowExceptionWhenRegistrationDtoIsNull(){
        //given
        UserRegistrationDto userRegistrationDtoNull = null;
        doThrow(new IllegalArgumentException("UserRegistrationDto cannot be null")).when(validationUtil).validateNotNull(userRegistrationDtoNull, "UserRegistrationDto");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.createNewUser(userRegistrationDtoNull));

        assertEquals("UserRegistrationDto cannot be null", exception.getMessage());

        verify(validationUtil).validateNotNull(userRegistrationDtoNull, "UserRegistrationDto");
        verifyNoMoreInteractions(userRepository, userRegistrationDtoMapper);
    }

    @Test
    void shouldThrowExceptionInCreateNewUserWhenEmailIsNull(){

        //given
        UserRegistrationDto userWithNullEmail = new UserRegistrationDto();
        userWithNullEmail.setEmail(null);
        userWithNullEmail.setPassword("somePassword");
        doNothing().when(validationUtil).validateNotNull(userWithNullEmail,"UserRegistrationDto");
        doThrow(new IllegalArgumentException("Email cannot be null"))
                .when(validationUtil)
                .validateEmailAndPassword(null,"somePassword");

        //then when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.createNewUser(userWithNullEmail));

        assertEquals("Email cannot be null", exception.getMessage());

        verify(validationUtil).validateNotNull(userWithNullEmail, "UserRegistrationDto");
        verify(validationUtil).validateEmailAndPassword(null, "somePassword");
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldThrowExceptionInCreateNewUserWhenPasswordIsNull(){

        //given
        UserRegistrationDto userWithNullPassword = new UserRegistrationDto();
        userWithNullPassword.setEmail("some@email.com");
        userWithNullPassword.setPassword(null);
        doNothing().when(validationUtil).validateNotNull(userWithNullPassword,"UserRegistrationDto");
        doThrow(new IllegalArgumentException("Password cannot be null"))
                .when(validationUtil)
                .validateEmailAndPassword("some@email.com",null);

        //then when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.createNewUser(userWithNullPassword));

        assertEquals("Password cannot be null", exception.getMessage());

        verify(validationUtil).validateNotNull(userWithNullPassword, "UserRegistrationDto");
        verify(validationUtil).validateEmailAndPassword("some@email.com", null);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldNotCallSaveWhenValidationFails(){
        //given
        UserRegistrationDto userWithNullEmail = new UserRegistrationDto();
        userWithNullEmail.setEmail(null);
        userWithNullEmail.setPassword("somePassword");
        doNothing().when(validationUtil).validateNotNull(userWithNullEmail,"UserRegistrationDto");
        doThrow(new IllegalArgumentException("Email cannot be null"))
                .when(validationUtil)
                .validateEmailAndPassword(null,"somePassword");

        //then when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.createNewUser(userWithNullEmail));

        assertEquals("Email cannot be null", exception.getMessage());

        verify(validationUtil).validateNotNull(userWithNullEmail, "UserRegistrationDto");
        verify(validationUtil).validateEmailAndPassword(null, "somePassword");
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldUpdateUserDetailsWhenAllFieldsPresent(){
        //given
        UserResponseDto dto = new UserResponseDto();
        dto.setFirstName("Jan");
        dto.setLastName("Nowak");
        dto.setEmail("jan.nowak@mail.com");
        dto.setPhoneNumber("123456789");

        User existingUser = new User();
        existingUser.setId(id);
        existingUser.setFirstName("Adam");
        existingUser.setLastName("Kowalski");
        existingUser.setEmail("old.email@mail.com");
        existingUser.setPhoneNumber("000000000");

        doNothing().when(validationUtil).validateNotNull(any(), anyString());
        doNothing().when(validationUtil).validateStringNotEmpty(anyString(), anyString());
        doNothing().when(validationUtil).validatePhoneNumber(anyString());

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));

        //when
        userService.updateUserDetails(dto,id);

        //then
        assertEquals("Jan", existingUser.getFirstName());
        assertEquals("Nowak", existingUser.getLastName());
        assertEquals("jan.nowak@mail.com", existingUser.getEmail());
        assertEquals("123456789", existingUser.getPhoneNumber());

        verify(userRepository).save(existingUser);
    }

    @Test
    void shouldUpdateOnlyFirstNameWhenOnlyFirstNameProvided(){
        //given
        UserResponseDto dto = new UserResponseDto();
        dto.setFirstName("Jan");
        dto.setLastName(null);
        dto.setEmail(null);
        dto.setPhoneNumber(null);

        User existingUser = new User();
        existingUser.setId(id);
        existingUser.setFirstName("Adam");
        existingUser.setLastName("Kowalski");
        existingUser.setEmail("old.email@mail.com");
        existingUser.setPhoneNumber("000000000");

        doNothing().when(validationUtil).validateNotNull(any(), anyString());
        doNothing().when(validationUtil).validateStringNotEmpty(anyString(), anyString());

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));

        //when
        userService.updateUserDetails(dto,id);

        //then
        assertEquals("Jan", existingUser.getFirstName());
        assertEquals("Kowalski", existingUser.getLastName());
        assertEquals("old.email@mail.com", existingUser.getEmail());
        assertEquals("000000000", existingUser.getPhoneNumber());

        verify(userRepository).save(existingUser);
    }
}