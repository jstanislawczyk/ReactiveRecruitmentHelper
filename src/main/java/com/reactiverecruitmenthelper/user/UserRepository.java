package com.reactiverecruitmenthelper.user;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {

    Mono<UserDetails> findByEmail(String email);
    Mono<User> getByEmail(String email);
}
