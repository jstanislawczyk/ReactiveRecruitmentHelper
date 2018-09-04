package com.reactiverecruitmenthelper.user;

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
        return user.switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundException("User not found [id=" + id + "]"))));
    }

    Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    Mono<User> saveUser(Mono<User> user) {
        return userRepository.insert(user).next();
    }

    Mono<Void> deleteUserById(String id) {
        return userRepository.findById(id)
                .transform(user -> errorIfEmpty(user, id))
                .flatMap(user -> userRepository.deleteById(id));
    }

    private <T> Mono<T> errorIfEmpty(Mono<T> source, String id) {
        return source.switchIfEmpty(Mono.error(new NotFoundException("User not found [id=" + id + "]")));
    }
}
