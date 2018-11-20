package com.reactiverecruitmenthelper.authentication;

import com.reactiverecruitmenthelper.exception.ConflictException;
import com.reactiverecruitmenthelper.exception.NotFoundException;
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

    Mono<User> authenticateUser(Mono<User> userMono) {
        return userMono
                .flatMap(this::isUserDataCorrect);
    }

    private Mono<User> isUserDataCorrect(User user) {
        return userRepository.getByEmail(user.getEmail())
                .flatMap(databaseUser -> {
                    if(isPasswordCorrect(user, databaseUser)) {
                        return Mono.just(databaseUser);
                    } else {
                        return Mono.empty();
                    }
                })
                .switchIfEmpty(Mono.error(new ConflictException("Authentication failed")));
    }

    private boolean isPasswordCorrect(User user, User databaseUser) {
        return passwordEncoder.matches(user.getPassword(), databaseUser.getPassword());
    }
}
