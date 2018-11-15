package com.reactiverecruitmenthelper.authentication;

import com.reactiverecruitmenthelper.user.User;
import com.reactiverecruitmenthelper.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private UserRepository userRepository;

    Mono<Boolean> authenticateUser(Mono<User> userMono) {
        return userMono
                .map(this::isUserDataCorrect);
    }

    private boolean isUserDataCorrect(User user) {
        return isEmailCorrect(user.getEmail()) && isPasswordCorrect(user.getPassword());
    }

    private boolean isEmailCorrect(String email) {
        return email.equals("admin@gmail.com");
    }

    private boolean isPasswordCorrect(String password) {
        return password.equals("admin");
    }
}
