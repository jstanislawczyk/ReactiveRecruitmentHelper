package com.reactiverecruitmenthelper.authentication;

import com.reactiverecruitmenthelper.user.User;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin
@AllArgsConstructor
public class AuthenticationController {

    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public Mono<Boolean> authenticateUser(@RequestBody Mono<User> userMono) {
        return authenticationService.authenticateUser(userMono);
    }
}
