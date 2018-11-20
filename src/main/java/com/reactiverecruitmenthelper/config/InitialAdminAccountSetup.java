package com.reactiverecruitmenthelper.config;

import com.reactiverecruitmenthelper.enums.Authority;
import com.reactiverecruitmenthelper.exception.ConflictException;
import com.reactiverecruitmenthelper.user.Role;
import com.reactiverecruitmenthelper.user.User;
import com.reactiverecruitmenthelper.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Component
@AllArgsConstructor
public class InitialAdminAccountSetup {

    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void createFirstAdminAccount() {
        Mono<User> admin = Mono.just(createAdmin());
        userRepository
                .insert(admin)
                .doOnError(throwable -> new ConflictException(""))
                .subscribe();
    }

    private User createAdmin() {
        return User.builder()
                ._id("1")
                .firstName("admin")
                .lastName("admin")
                .email("admin@mail.com")
                .password(passwordEncoder.encode("admin"))
                .roles(Arrays.asList(
                        new Role(Authority.ADMIN),
                        new Role(Authority.RECRUITER)
                ))
                .active(true)
                .build();
    }
}
