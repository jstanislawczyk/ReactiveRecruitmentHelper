package com.reactiverecruitmenthelper.user;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class UserDtoConverter {

    private PasswordEncoder passwordEncoder;

    Mono<UserDto> userMonoToDtoMonoWithRoles(Mono<User> userMono) {
        return userMono.flatMap(user ->
                Mono.just(UserDto.builder()
                        ._id(user.get_id())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .roles(user.getRoles())
                        .build()
                ));
    }

    Mono<User> userMonoFromDtoMonoWithRoles(Mono<UserDto> userDtoMono) {
        return userDtoMono.flatMap(userDto ->
                Mono.just(User.builder()
                        ._id(userDto.get_id())
                        .firstName(userDto.getFirstName())
                        .lastName(userDto.getLastName())
                        .email(userDto.getEmail())
                        .password(passwordEncoder.encode(userDto.getPassword()))
                        .roles(userDto.getRoles())
                        .build()
                ));
    }
}
