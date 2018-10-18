package com.reactiverecruitmenthelper.user;

import com.reactiverecruitmenthelper.enums.Authority;
import com.reactiverecruitmenthelper.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("UnassignedFluxMonoInstance")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldGetUserById() {
        var id = "1";

        when(userRepository.findById(id)).thenReturn(Mono.just(users().get(0)));

        var userFromRepository = userRepository.findById(id).block();
        var userFromService = userService.getUserById(id).block();

        assertAll(
                () -> assertNotNull(userFromRepository),
                () -> assertNotNull(userFromService),
                () -> assertEquals(userFromService.getFirstName(), userFromRepository.getFirstName()),
                () -> assertEquals(userFromService.getLastName(), userFromRepository.getLastName()),
                () -> assertEquals(userFromService.getEmail(), userFromRepository.getEmail()),
                () -> assertEquals(userFromService.getPassword(), userFromRepository.getPassword()),
                () -> assertEquals(userFromService.getAuthorities(), userFromRepository.getAuthorities()),
                () -> assertEquals(userFromService.getRoles(), userFromRepository.getRoles())
        );

        verify(userRepository, times(2)).findById(id);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldThrowErrorWhenIdIsNotFound() {
        var id = "2";
        when(userRepository.findById(id)).thenReturn(Mono.empty());

        assertThrows(NotFoundException.class, () -> userService.getUserById(id).block());

        verify(userRepository, times(1)).findById(id);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Flux.fromIterable(users()));

        assertEquals(userService.getAllUsers(), userRepository.findAll());

        verify(userRepository, times(2)).findAll();
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldGetEmptyFlux() {
        when(userRepository.findAll()).thenReturn(Flux.empty());

        assertEquals(userService.getAllUsers(), userRepository.findAll());

        verify(userRepository, times(2)).findAll();
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldDeleteUser() {
        var id = "1";

        when(userRepository.findById(id)).thenReturn(Mono.just(users().get(0)));
        when(userRepository.deleteById(id)).thenReturn(Mono.empty());

        assertNull(userService.deleteUserById(id).block());

        userService.deleteUserById(id);

        verify(userRepository, times(2)).deleteById(id);
    }

    @Test
    void shouldNotDeleteIfUserDoesNotExist() {
        var id = "1";

        when(userRepository.findById(id)).thenReturn(Mono.empty());
        assertThrows(NullPointerException.class, () -> userService.deleteUserById(id));

        verify(userRepository, times(1)).deleteById(id);
    }

    private List<User> updateUsers() {
        List<User> users = new ArrayList<>();
        users.add(User.builder()
                ._id("1")
                .firstName("Jan")
                .lastName("Kowalski")
                .email("janKowalski1@mail.com")
                .password("password")
                .roles(new ArrayList<>())
                .active(true)
                .build());

        users.add(User.builder()
                .firstName("Updated")
                .lastName("Updated")
                .build());

        users.add(User.builder()
                ._id("1")
                .firstName("Updated")
                .lastName("Updated")
                .email("janKowalski1@mail.com")
                .password("password")
                .roles(new ArrayList<>())
                .active(true)
                .build());

        return users;
    }

    private List<User> users() {
        List<User> users = new ArrayList<>();

        users.add(User.builder()
                ._id("1")
                .firstName("Jan")
                .lastName("Kowalski")
                .email("janKowalski1@mail.com")
                .password("password")
                .roles(new ArrayList<>())
                .active(true)
                .build());

        users.add(User.builder()
                ._id("2")
                .firstName("Jan")
                .lastName("Kowalski")
                .email("janKowalski1@mail.com")
                .password("password")
                .roles(Collections.singletonList(new Role(Authority.ADMIN)))
                .active(true)
                .build());

        users.add(User.builder()
                ._id("3")
                .firstName("Jan")
                .lastName("Kowalski")
                .email("janKowalski1@mail.com")
                .password("password")
                .roles(Collections.singletonList(new Role(Authority.RECRUITER)))
                .active(true)
                .build());

        users.add(User.builder()
                ._id("4")
                .firstName("Jan")
                .lastName("Kowalski")
                .email("janKowalski1@mail.com")
                .password("password")
                .roles(Collections.singletonList(new Role(Authority.RECRUITER)))
                .active(true)
                .build());

        users.add(User.builder()
                .firstName("Jan")
                .lastName("Kowalski")
                .email("janKowalski1@mail.com")
                .password("password")
                .roles(Arrays.asList(
                        new Role(Authority.ADMIN),
                        new Role(Authority.RECRUITER)
                ))
                .active(true)
                .build());

        return users;
    }
}
