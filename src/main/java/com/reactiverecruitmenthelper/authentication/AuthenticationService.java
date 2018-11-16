package com.reactiverecruitmenthelper.authentication;

import com.reactiverecruitmenthelper.user.User;
import com.reactiverecruitmenthelper.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class AuthenticationService {


    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    Mono<Boolean> authenticateUser(Mono<User> userMono) {
        return userMono
                .flatMap(this::isUserDataCorrect)
                .switchIfEmpty(Mono.just(false));
    }

    private Mono<Boolean> isUserDataCorrect(User user) {
        return userRepository.getByEmail(user.getEmail())
                .map(databaseUser -> isPasswordCorrect(user, databaseUser));
    }

    private boolean isPasswordCorrect(User user, User databaseUser) {
        return passwordEncoder.matches(user.getPassword(), databaseUser.getPassword());
    }
}
