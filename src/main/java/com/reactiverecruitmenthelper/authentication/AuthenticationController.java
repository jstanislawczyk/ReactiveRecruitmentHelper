package com.reactiverecruitmenthelper.authentication;

import com.reactiverecruitmenthelper.user.User;
import com.reactiverecruitmenthelper.user.UserDto;
import com.reactiverecruitmenthelper.user.UserDtoConverter;
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
    private UserDtoConverter userDtoConverter;

    @PostMapping("/login")
    public Mono<UserDto> authenticateUser(@RequestBody Mono<UserDto> userMonoDto) {
        Mono<User> userMono = userDtoConverter.userMonoFromDtoMonoWithRoles(userMonoDto);
        return userDtoConverter.userMonoToDtoMonoWithRoles(authenticationService.authenticateUser(userMono));
    }
}
