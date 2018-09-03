package com.reactiverecruitmenthelper.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private UserDtoConverter dtoConverter;

    Mono<UserDto> getUserById(String id) {
        Mono<User> user = userRepository.findById(id);
        return dtoConverter.userMonoToDtoMonoWithRoles(user);
    }

    Flux<UserDto> getAllUsers() {
        return userRepository.findAll().flatMap(user -> dtoConverter.userMonoToDtoMono(Mono.just(user)));
    }

    Mono<User> saveUser(UserDto userDto) {
        Mono<User> user = dtoConverter.userFromDtoWithRoles(Mono.just(userDto));
        userRepository.insert(user);
        return userRepository.insert(user).next();
    }

    Mono<Void> deleteUserById(String id) {
        userRepository.deleteById(id).doOnError(throwable -> {
            throw new RuntimeException("User not found");
        });

        return Mono.empty();
    }
}
