package com.reactiverecruitmenthelper.user;

import com.reactiverecruitmenthelper.helper.Page;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.reactiverecruitmenthelper.helper.Page.DEFAULT_PAGE_SIZE;
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

    @GetMapping("/all")
    @ResponseStatus(OK)
    public Flux<UserDto> getAllUsers() {
        return dtoConverter.userFluxToDtoFluxWithRoles(userService.getAllUsers());
    }

    @GetMapping
    @ResponseStatus(OK)
    public Mono<Page<UserDto>> getUsersPage(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = DEFAULT_PAGE_SIZE) int size) {
        return userService.getUsersPage(page, size);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Mono<UserDto> saveUser(@RequestBody Mono<UserDto> userDto) {
        Mono<User> userMono = userService.saveUser(dtoConverter.userMonoFromDtoMonoWithRoles(userDto));
        return dtoConverter.userMonoToDtoMonoWithRoles(userMono);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public Mono<Void> deleteUserById(@PathVariable String id) {
        return userService.deleteUserById(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(OK)
    public Mono<UserDto> updateUserById(@PathVariable String id, @RequestBody Mono<UserDto> userDto) {
        Mono<User> user = userService.updateUser(id, dtoConverter.userMonoFromDtoMonoWithRoles(userDto));
        return dtoConverter.userMonoToDtoMonoWithRoles(user);
    }
}
