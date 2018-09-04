package com.reactiverecruitmenthelper.user;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UserDtoConverter {
    Mono<UserDto> userMonoToDtoMonoWithRoles(Mono<User> userMono) {
        return userMono.flatMap(user ->
                Mono.just(UserDto.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .roles(user.getRoles())
                        .build()
                ));
    }

    Mono<User> userFromDtoWithRoles(Mono<UserDto> userDtoMono) {
        return userDtoMono.flatMap(userDto ->
                Mono.just(User.builder()
                        .id(userDto.getId())
                        .firstName(userDto.getFirstName())
                        .lastName(userDto.getLastName())
                        .email(userDto.getEmail())
                        .password(userDto.getPassword())
                        .roles(userDto.getRoles())
                        .build()
                ));
    }
}
