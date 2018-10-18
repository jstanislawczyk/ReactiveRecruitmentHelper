package com.reactiverecruitmenthelper.logger;

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

    @Pointcut("execution(* com.reactiverecruitmenthelper.user.UserController.getUserById(String)) && args(id)")
    public void getUserById(String id) {}

    @Pointcut("execution(* com.reactiverecruitmenthelper.user.UserController.deleteUserById(String)) && args(id)")
    public void deleteUserById(String id) {}

    @Pointcut("execution(* com.reactiverecruitmenthelper.user.UserController.saveUser(*))")
    public void saveUser() {}

    @Pointcut("execution(* com.reactiverecruitmenthelper.user.UserController.updateUserById(*))")
    public void updateUserById() {}


    @Before(value = "getUserById(id)", argNames = "id")
    public void logBeforeGetUserById(String id) {
        log.info("ReactiveRecruitmentHelper | Admin attempts to get user [id = {}]", id);
    }

    @AfterReturning("getUserById(id)")
    public void logAfterGetUserById(String id) {
        log.info("ReactiveRecruitmentHelper | Admin received user [id = {}]", id);
    }

    @Before("getAllUsers()")
    public void logBeforeGetAllUsers() {
        log.info("ReactiveRecruitmentHelper | Admin attempts to get all users");
    }

    @AfterReturning("getAllUsers()")
    public void logAfterGetAllUsers() {
        log.info("ReactiveRecruitmentHelper | Admin received all users");
    }

    @Before(value = "deleteUserById(id)", argNames = "id")
    public void logBeforeDeleteUserById(String id) {
        log.info("ReactiveRecruitmentHelper | Admin attempts to delete user [id = {}]", id);
    }

    @AfterReturning(value = "deleteUserById(id)", argNames = "id")
    public void logAfterDeleteUserById(String id) {
        log.info("ReactiveRecruitmentHelper | Admin deleted user [id = {}]", id);
    }

    @Before("saveUser()")
    public void logBeforeSaveUser() {
        log.info("ReactiveRecruitmentHelper | Admin attempts to save user");
    }

    @AfterReturning("saveUser()")
    public void logAfterSaveUser() {
        log.info("ReactiveRecruitmentHelper | Admin saved user");
    }

    @Before("updateUserById()")
    public void logBeforeUpdateUserById() {
        log.info("ReactiveRecruitmentHelper | Admin attempts to update user");
    }

    @AfterReturning("updateUserById()")
    public void logAfterUpdateUserById() {
        log.info("ReactiveRecruitmentHelper | Admin updated user");
    }
}
