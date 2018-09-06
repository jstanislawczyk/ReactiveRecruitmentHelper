package com.reactiverecruitmenthelper.logger;

import com.reactiverecruitmenthelper.config.SecurityConfig;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class UserLogger {

    @Pointcut("execution(* com.reactiverecruitmenthelper.user.UserController.getAllUsers())")
    public void getAllUsers() {}

    @Before("getAllUsers()")
    public void logBeforeGetAllUsers() {
        log.info("ReactiveRecruitmentHelper | Admin [email = {}] attempts to get all users", SecurityConfig.getCurrentLoggedInUserEmail());
    }

    @AfterReturning("getAllUsers()")
    public void logAfterGetAllUsers() {
        log.info("ReactiveRecruitmentHelper | Admin [email = {}] received all users", SecurityConfig.getCurrentLoggedInUserEmail());
    }
}
