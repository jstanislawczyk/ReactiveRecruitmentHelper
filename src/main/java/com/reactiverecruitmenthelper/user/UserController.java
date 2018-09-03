package com.reactiverecruitmenthelper.user;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.*;

@RestController
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @GetMapping("/users/{id}")
    @ResponseStatus(OK)
    public Mono<UserDto> getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @GetMapping("/users")
    @ResponseStatus(OK)
    public Flux<UserDto> getUserAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/users")
    @ResponseStatus(CREATED)
    public Mono<User> saveUser(@RequestBody UserDto userDto) {
        return userService.saveUser(userDto);
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(NO_CONTENT)
    public Mono<Void> deleteUserById(@PathVariable String id) {
        return userService.deleteUserById(id);
    }
}
