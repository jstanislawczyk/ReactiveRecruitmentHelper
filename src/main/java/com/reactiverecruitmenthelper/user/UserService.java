package com.reactiverecruitmenthelper.user;

import com.reactiverecruitmenthelper.exception.ConflictException;
import com.reactiverecruitmenthelper.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    Mono<User> getUserById(String id) {
        Mono<User> user = userRepository.findById(id);
        return user.transform(userMono -> throwErrorIfEmpty(userMono, id));
    }

    Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    Mono<User> saveUser(Mono<User> userMono) {
        return userMono
                .flatMap(this::validEmailUniqueness)
                .flatMap(this::encodePassword)
                .flatMap(user -> userRepository.insert(user))
                .doOnError(throwable -> {throw new ConflictException("Email already exists");});
    }

    Mono<Void> deleteUserById(String id) {
        return userRepository.findById(id)
                .transform(user -> throwErrorIfEmpty(user, id))
                .flatMap(user -> userRepository.deleteById(id));
    }

    Mono<User> updateUser(String id, Mono<User> newUserMono) {
        return userRepository.findById(id)
                .flatMap(this::validEmailUniqueness)
                .transform(user -> throwErrorIfEmpty(user, id))
                .transform(user -> updateEntity(newUserMono, user))
                .flatMap(user -> userRepository.save(user));
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
        return source.switchIfEmpty(Mono.error(new NotFoundException("User not found [_id = " + id + "]")));
    }

    private Mono<User> updateEntity(Mono<User> newUserMono, Mono<User> oldUserMono) {
        return newUserMono.flatMap(updatedUser -> oldUserMono.flatMap(
                oldUser -> {
                    Optional.ofNullable(updatedUser.getFirstName())
                            .ifPresent(oldUser::setFirstName);

                    Optional.ofNullable(updatedUser.getLastName())
                            .ifPresent(oldUser::setLastName);

                    Optional.ofNullable(updatedUser.getPassword())
                            .ifPresent(s -> oldUser.setPassword(passwordEncoder.encode(s)));

                    Optional.ofNullable(updatedUser.getRoles())
                            .ifPresent(oldUser::setRoles);

                    return Mono.just(oldUser);
                }
        ));
    }
}
