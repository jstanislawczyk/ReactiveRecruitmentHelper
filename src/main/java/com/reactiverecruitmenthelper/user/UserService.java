package com.reactiverecruitmenthelper.user;

import com.reactiverecruitmenthelper.exception.ConflictException;
import com.reactiverecruitmenthelper.exception.NotFoundException;
import com.reactiverecruitmenthelper.helper.Page;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserDtoConverter userDtoConverter;

    Mono<User> getUserById(String id) {
        Mono<User> user = userRepository.findById(id);
        return user.transform(userMono -> throwErrorIfEmpty(userMono, id));
    }

    Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    Mono<Page<UserDto>> getUsersPage(int page, int size) {
        return userRepository.findAll()
                .collectList()
                .map(usersList ->
                        new Page<>(
                                usersList
                                    .stream()
                                    .skip(page * size)
                                    .limit(size)
                                    .map(userDtoConverter::userToDtoWithRoles)
                                    .collect(Collectors.toList()),
                                page,
                                size,
                                usersList.size()
                        )
                );
    }

    Mono<User> saveUser(Mono<User> userMono) {
        return userMono
                .flatMap(this::validEmailUniqueness)
                .flatMap(this::encodePassword)
                .flatMap(user -> userRepository.insert(user))
                .doOnError(throwable -> {
                    throw new ConflictException("Email already exists");
                });
    }

    Mono<Void> deleteUserById(String id) {
        return userRepository.findById(id)
                .transform(user -> throwErrorIfEmpty(user, id))
                .then(userRepository.deleteById(id));
    }

    Mono<User> updateUser(String id, Mono<User> newUserMono) {
        return userRepository.findById(id)
                .transform(user -> throwErrorIfEmpty(user, id))
                .transform(user -> updateEntity(newUserMono, user))
                .flatMap(userRepository::save);
    }

    private Mono<User> validEmailUniqueness(User user) {
        return userRepository.getByEmail(user.getEmail())
                .switchIfEmpty(Mono.just(user));
    }

    private Mono<User> encodePassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return Mono.just(user);
    }

    private Mono<User> throwErrorIfEmpty(Mono<User> source, String id) {
        return source.switchIfEmpty(Mono.error(new NotFoundException("User [_id = " + id + "] not found")));
    }

    private Mono<User> updateEntity(Mono<User> newUserMono, Mono<User> oldUserMono) {
        return newUserMono.flatMap(updatedUser -> oldUserMono.flatMap(
                oldUser -> {
                    Optional.ofNullable(updatedUser.getFirstName())
                            .ifPresent(oldUser::setFirstName);

                    Optional.ofNullable(updatedUser.getLastName())
                            .ifPresent(oldUser::setLastName);

                    Optional.of(updatedUser.isActive())
                            .ifPresent(oldUser::setActive);

                    Optional.ofNullable(updatedUser.getPassword())
                            .ifPresent(password -> oldUser.setPassword(passwordEncoder.encode(password)));

                    Optional.ofNullable(updatedUser.getRoles())
                            .ifPresent(oldUser::setRoles);

                    return Mono.just(oldUser);
                }
        ));
    }
}
