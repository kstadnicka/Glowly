package ks.glowlyapp.user;

import ks.glowlyapp.user.dto.UserRegistrationDto;
import ks.glowlyapp.user.dto.UserResponseDto;
import ks.glowlyapp.validation.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    private final String email2 = "another@example.com";
    private User user;
    private User user2;
    private UserRegistrationDto userRegistrationDto;
    private UserResponseDto userResponseDto;
    private UserResponseDto userResponseDto2;

    @BeforeEach
    void  setUp(){
        user = new User();
        user.setEmail(email);
        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail(email);
        user2 = new User();
        user2.setEmail(email2);

        userResponseDto = new UserResponseDto();
        userResponseDto.setEmail(email);
        userResponseDto2 = new UserResponseDto();
        userResponseDto2.setEmail(email2);
    }
    @Test
    void shouldReturnUserRegistrationDtoWhenUserExist(){
        //given
        doNothing().when(validationUtil).validateNotNull(email, "Email");
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
        when(userRegistrationDtoMapper.map(user)).thenReturn(userRegistrationDto);

        //when
        Optional<UserRegistrationDto> result = userService.findUserByEmail(email);

        //then
        assertTrue(result.isPresent());
        assertEquals(userRegistrationDto, result.get());

        verify(validationUtil).validateNotNull(email,"Email");
        verify(userRepository).findUserByEmail(email);
        verify(userRegistrationDtoMapper).map(user);
    }

    @Test
    void shouldReturnEmptyOptionalWhenUserIsNotFound(){
        //given
        doNothing().when(validationUtil).validateNotNull(email,"Email");
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());

        //when
        Optional<UserRegistrationDto> result = userService.findUserByEmail(email);

        //then
        assertTrue(result.isEmpty());

        verify(validationUtil).validateNotNull(email,"Email");
        verify(userRepository).findUserByEmail(email);
        verify(userRegistrationDtoMapper, never()).map(any());
    }

    @Test
    void shouldThrowExceptionWhenEmailIsNull(){
        String nullEmail = null;

        //given
        doThrow(new IllegalArgumentException("Email cannot be null")).when(validationUtil).validateNotNull(nullEmail,"Email");

        //then when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                ()-> userService.findUserByEmail(nullEmail));

        assertEquals("Email cannot be null", exception.getMessage());

        verify(validationUtil).validateNotNull(nullEmail,"Email");
        verifyNoMoreInteractions(userRepository,userRegistrationDtoMapper);
    }

    @Test
    void shouldReturnAllUsersWhenExist(){

        //given
        List<User> users = List.of(user,user2);
        when(userRepository.findAll()).thenReturn(users);
        when(userResponseDtoMapper.map(user)).thenReturn(userResponseDto);
        when(userResponseDtoMapper.map(user2)).thenReturn(userResponseDto2);

        //when
        List<UserResponseDto> allUsers = userService.getAllUsers();

        //then
        assertEquals(2,allUsers.size());
        assertEquals(userResponseDto,allUsers.get(0));
        assertEquals(userResponseDto2,allUsers.get(1));

        verify(userRepository).findAll();
        verify(userResponseDtoMapper).map(user);
        verify(userResponseDtoMapper).map(user2);
    }

    @Test
    void shouldReturnEmptyListWhenUsersNotExist(){

        //given
        List<User> users = List.of();
        when(userRepository.findAll()).thenReturn(users);

        //when
        List<UserResponseDto> allUsers = userService.getAllUsers();

        //then
        assertEquals(0,allUsers.size());
        verify(userRepository).findAll();

    }

    @Test
    void shouldReturnUserByIdWhenExist(){
        //given
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userResponseDtoMapper.map(user)).thenReturn(userResponseDto);

        //when
        Optional<UserResponseDto> result = userService.getUserById(id);

        //then
        assertTrue(result.isPresent());
        assertEquals(userResponseDto,result.get());

        verify(userRepository).findById(id);
        verify(userResponseDtoMapper).map(user);
    }

    @Test
    void shouldReturnEmptyOptionalWhenUserNotExist(){
        //given
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        //when
        Optional<UserResponseDto> result = userService.getUserById(id);

        //then
        assertTrue(result.isEmpty());

        verify(userRepository).findById(id);
        verify(userResponseDtoMapper, never()).map(any());
    }
}