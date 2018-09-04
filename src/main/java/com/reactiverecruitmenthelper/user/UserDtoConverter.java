package com.reactiverecruitmenthelper.user;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UserDtoConverter {
    Mono<UserDto> userMonoToDtoMonoWithRoles(Mono<User> user) {
        return user.flatMap(value ->
                Mono.just(UserDto.builder()
                        .id(value.getId())
                        .firstName(value.getFirstName())
                        .lastName(value.getLastName())
                        .email(value.getEmail())
                        .password(value.getPassword())
                        .roles(value.getRoles())
                        .build()
                ));
    }

    Mono<User> userFromDtoWithRoles(Mono<UserDto> userDto) {
        return userDto.flatMap(value ->
                Mono.just(User.builder()
                        .id(value.getId())
                        .firstName(value.getFirstName())
                        .lastName(value.getLastName())
                        .email(value.getEmail())
                        .password(value.getPassword())
                        .roles(value.getRoles())
                        .build()
                ));
    }
}
