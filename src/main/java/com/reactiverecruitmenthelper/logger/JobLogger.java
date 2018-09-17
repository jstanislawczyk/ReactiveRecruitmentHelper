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
public class JobLogger {

    @Pointcut("execution(* com.reactiverecruitmenthelper.application.JobApplicationController.getAllJobApplications())")
    public void getAllJobApplications() {}

    @Pointcut("execution(* com.reactiverecruitmenthelper.application.JobApplicationController.getJobApplicationById(String)) && args(id)")
    public void getJobApplicationId(String id) {}

    @Pointcut("execution(* com.reactiverecruitmenthelper.application.JobApplicationController.deleteJobApplicationById(String)) && args(id)")
    public void deleteJobApplicationId(String id) {}

    @Pointcut("execution(* com.reactiverecruitmenthelper.application.JobApplicationController.saveJobApplication(*))")
    public void saveJobApplication() {}


    @Before("getJobApplicationId(id)")
    public void logBeforeGetJobApplicationById(String id) {
        log.info("ReactiveRecruitmentHelper | Admin attempts to get job application [id = {}]", id);
    }

    @AfterReturning("getJobApplicationId(id)")
    public void logAfterGetJobApplicationById(String id) {
        log.info("ReactiveRecruitmentHelper | Admin received job application [id = {}]", id);
    }

    @Before("getAllJobApplications()")
    public void logBeforeGetAllJobApplications() {
        log.info("ReactiveRecruitmentHelper | Admin attempts to get all job applications");
    }

    @AfterReturning("getAllJobApplications()")
    public void logAfterGetAllJobApplications() {
        log.info("ReactiveRecruitmentHelper | Admin received all job applications");
    }

    @Before("deleteJobApplicationId(id)")
    public void logBeforeDeleteJobApplicationById(String id) {
        log.info("ReactiveRecruitmentHelper | Admin attempts to delete job application [id = {}]", id);
    }

    @AfterReturning("deleteJobApplicationId(id)")
    public void logAfterDeleteJobApplicationById(String id) {
        log.info("ReactiveRecruitmentHelper | Admin deleted job application [id = {}]", id);
    }

    @Before("saveJobApplication()")
    public void logBeforeSaveJobApplication() {
        log.info("ReactiveRecruitmentHelper | Admin attempts to save job application");
    }

    @AfterReturning("saveJobApplication()")
    public void logAfterSaveJobApplication() {
        log.info("ReactiveRecruitmentHelper | Admin saved job application");
    }
}
