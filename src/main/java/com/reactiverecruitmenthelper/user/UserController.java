package com.reactiverecruitmenthelper.user;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;
    private UserDtoConverter dtoConverter;

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public Mono<UserDto> getUserById(@PathVariable String id) {
        return dtoConverter.userMonoToDtoMonoWithRoles(userService.getUserById(id));
    }

    @GetMapping
    @ResponseStatus(OK)
    public Flux<UserDto> getAllUsers() {
        return userService.getAllUsers().flatMap(user -> dtoConverter.userMonoToDtoMonoWithRoles(Mono.just(user)));
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Mono<UserDto> saveUser(@RequestBody Mono<UserDto> userDto) {
        Mono<User> user = userService.saveUser(dtoConverter.userFromDtoWithRoles(userDto));
        return dtoConverter.userMonoToDtoMonoWithRoles(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public Mono<Void> deleteUserById(@PathVariable String id) {
        return userService.deleteUserById(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(OK)
    public Mono<UserDto> updateUserById(@PathVariable String id, @RequestBody Mono<UserDto> userDto) {
        Mono<User> user = userService.updateUser(id, dtoConverter.userFromDtoWithRoles(userDto));
        return dtoConverter.userMonoToDtoMonoWithRoles(user);
    }
}
