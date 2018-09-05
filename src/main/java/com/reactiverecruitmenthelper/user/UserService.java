package com.reactiverecruitmenthelper.user;

import com.reactiverecruitmenthelper.exception.ConflictException;
import com.reactiverecruitmenthelper.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    Mono<User> getUserById(String id) {
        Mono<User> user = userRepository.findById(id);
        return user.transform(userMono -> throwErrorIfEmpty(userMono, id));
    }

    Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    Mono<User> saveUser(Mono<User> userMono) {
        return userRepository
                .insert(userMono)
                .flatMap(this::validEmailUniqueness)
                .next();
    }

    Mono<Void> deleteUserById(String id) {
        return userRepository.findById(id)
                .transform(user -> throwErrorIfEmpty(user, id))
                .flatMap(user -> userRepository.deleteById(id));
    }

    Mono<User> updateUser(String id, Mono<User> newUserMono) {
        return userRepository.findById(id)
                .transform(user -> throwErrorIfEmpty(user, id))
                .transform(user -> updateEntity(newUserMono, user))
                .flatMap(user -> userRepository.save(user));
    }

    private Mono<User> validEmailUniqueness(User user) {
        return userRepository.findByEmail(user.getEmail())
                .doOnError(throwable -> {throw new ConflictException("Email already exists");});
    }

    private Mono<User> throwErrorIfEmpty(Mono<User> source, String id) {
        return source.switchIfEmpty(Mono.error(new NotFoundException("User not found [_id = " + id + "]")));
    }

    private Mono<User> updateEntity(Mono<User> newUserMono, Mono<User> oldUserMono) {
        return newUserMono.flatMap(updatedUser -> oldUserMono.flatMap(
                oldUser -> {
                    oldUser.setEmail(updatedUser.getEmail());
                    oldUser.setFirstName(updatedUser.getFirstName());
                    oldUser.setLastName(updatedUser.getLastName());
                    oldUser.setPassword(updatedUser.getPassword());
                    return Mono.just(oldUser);
                }
        ));
    }
}
