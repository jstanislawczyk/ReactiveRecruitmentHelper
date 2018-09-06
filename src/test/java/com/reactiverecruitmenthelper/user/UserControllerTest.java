package com.reactiverecruitmenthelper.user;

import com.reactiverecruitmenthelper.enums.Authority;
import com.reactiverecruitmenthelper.exception.ConflictException;
import com.reactiverecruitmenthelper.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@SuppressWarnings("UnassignedFluxMonoInstance")
@WebFluxTest(UserController.class)
@ExtendWith(SpringExtension.class)
class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService userService;

    @MockBean
    private UserDtoConverter dtoConverter;

    @Test
    void shouldGetUserDtoById() {
        String id = "1";
        Mono<User> userMono = Mono.just(users().get(0));
        Mono<UserDto> userDtoMono = Mono.just(usersDtos().get(0));
        UserDto userDto = usersDtos().get(0);

        when(userService.getUserById(id)).thenReturn(userMono);
        when(dtoConverter.userMonoToDtoMonoWithRoles(userMono)).thenReturn(userDtoMono);

        webTestClient.get().uri("/users/{id}", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDto.class).isEqualTo(userDto);

        verify(userService, times(1)).getUserById(id);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void shouldNotGetUserDtoById() {
        String id = "1";
        Mono<User> userMono = Mono.empty();
        Mono<UserDto> userDtoMono = Mono.empty();

        when(userService.getUserById(id)).thenThrow(new NotFoundException("User [_id = " + id + "] not found"));
        when(dtoConverter.userMonoToDtoMonoWithRoles(userMono)).thenReturn(userDtoMono);

        webTestClient.get().uri("/users/{id}", id)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().json("{\"code\":404,\"message\":\"User [_id = 1] not found\"}");

        verify(userService, times(1)).getUserById(id);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void shouldGetAllUsers() {
        Flux<User> usersFlux = Flux.fromIterable(users());
        Flux<UserDto> userDtosFlux = Flux.fromIterable(usersDtos());

        when(userService.getAllUsers()).thenReturn(usersFlux);
        when(dtoConverter.userFluxToDtoFluxWithRoles(usersFlux)).thenReturn(userDtosFlux);

        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus().isOk();

        verify(userService, times(1)).getAllUsers();
        verifyNoMoreInteractions(userService);
    }

    @Test
    void shouldDeleteUserById() {
        String id = "1";

        webTestClient.delete().uri("/users/{id}", id)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();

        verify(userService, times(1)).deleteUserById(id);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void shouldNotDeleteUserByIdWhenUserDoesNotExists() {
        String id = "1";

        when(userService.deleteUserById(id))
                .thenThrow(new NotFoundException("User [_id = " + id + "] not found"));

        webTestClient.delete().uri("/users/{id}", id)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().json("{\"code\":404,\"message\":\"User [_id = 1] not found\"}");

        verify(userService, times(1)).deleteUserById(id);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void shouldSaveUser() {
        String id = "4";
        Mono<User> unsavedUserMono = Mono.just(users().get(3));
        Mono<User> savedUserMono = Mono.just(users().get(3));
        Mono<UserDto> unsavedUserMonoDto = Mono.just(usersDtos().get(3));
        Mono<UserDto> savedUserMonoDto = Mono.just(usersDtos().get(3));

        savedUserMono.subscribe(user -> user.set_id(id));
        savedUserMonoDto.subscribe(userDto -> userDto.set_id(id));

        when(userService.saveUser(unsavedUserMono)).thenReturn(savedUserMono);
        when(dtoConverter.userMonoFromDtoMonoWithRoles(unsavedUserMonoDto)).thenReturn(unsavedUserMono);
        when(dtoConverter.userMonoToDtoMonoWithRoles(savedUserMono)).thenReturn(savedUserMonoDto);

        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(usersDtos().get(3)))
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void shouldUpdateUser() {
        String id = "1";

        when(dtoConverter.userMonoFromDtoMonoWithRoles(any()))
                .thenReturn(Mono.just(updateUsers().get(0)));

        when(dtoConverter.userMonoToDtoMonoWithRoles(any()))
                .thenReturn(Mono.just(updateUsersDto().get(1)));

        webTestClient.patch().uri("/users/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromObject(updateUsers().get(0)))
            .exchange()
            .expectStatus().isOk();
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
                ._id("1")
                .firstName("Updated")
                .lastName("Updated")
                .email("updated@mail.com")
                .password("password")
                .roles(new ArrayList<>())
                .active(true)
                .build());

        return users;
    }

    private List<UserDto> updateUsersDto() {
        List<UserDto> usersDto = new ArrayList<>();
        usersDto.add(UserDto.builder()
                ._id("1")
                .firstName("Jan")
                .lastName("Kowalski")
                .email("janKowalski1@mail.com")
                .password("password")
                .roles(new ArrayList<>())
                .active(true)
                .build());

        usersDto.add(UserDto.builder()
                ._id("1")
                .firstName("Updated")
                .lastName("Updated")
                .email("updated@mail.com")
                .password("password")
                .roles(new ArrayList<>())
                .active(true)
                .build());

        return usersDto;
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

    private List<UserDto> usersDtos() {
        List<UserDto> userDtos = new ArrayList<>();

        userDtos.add(UserDto.builder()
                ._id("1")
                .firstName("Jan")
                .lastName("Kowalski")
                .email("janKowalski1@mail.com")
                .password("password")
                .roles(new ArrayList<>())
                .build());

        userDtos.add(UserDto.builder()
                ._id("2")
                .firstName("Jan")
                .lastName("Kowalski")
                .email("janKowalski1@mail.com")
                .password("password")
                .roles(Collections.singletonList(new Role(Authority.ADMIN)))
                .build());

        userDtos.add(UserDto.builder()
                ._id("3")
                .firstName("Jan")
                .lastName("Kowalski")
                .email("janKowalski1@mail.com")
                .password("password")
                .roles(Collections.singletonList(new Role(Authority.RECRUITER)))
                .build());

        userDtos.add(UserDto.builder()
                .firstName("Jan")
                .lastName("Kowalski")
                .email("janKowalski1@mail.com")
                .password("password")
                .roles(Arrays.asList(
                        new Role(Authority.ADMIN),
                        new Role(Authority.RECRUITER)
                ))
                .build());

        return userDtos;
    }
}
