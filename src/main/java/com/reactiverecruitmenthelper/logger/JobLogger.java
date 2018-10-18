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


    @Before(value = "getJobApplicationId(id)", argNames = "id")
    public void logBeforeGetJobApplicationById(String id) {
        log.info("ReactiveRecruitmentHelper | Recruiter attempts to get job application [id = {}]", id);
    }

    @AfterReturning(value = "getJobApplicationId(id)", argNames = "id")
    public void logAfterGetJobApplicationById(String id) {
        log.info("ReactiveRecruitmentHelper | Recruiter received job application [id = {}]", id);
    }

    @Before("getAllJobApplications()")
    public void logBeforeGetAllJobApplications() {
        log.info("ReactiveRecruitmentHelper | Recruiter attempts to get all job applications");
    }

    @AfterReturning("getAllJobApplications()")
    public void logAfterGetAllJobApplications() {
        log.info("ReactiveRecruitmentHelper | Recruiter received all job applications");
    }

    @Before(value = "deleteJobApplicationId(id)", argNames = "id")
    public void logBeforeDeleteJobApplicationById(String id) {
        log.info("ReactiveRecruitmentHelper | Recruiter attempts to delete job application [id = {}]", id);
    }

    @AfterReturning(value = "deleteJobApplicationId(id)", argNames = "id")
    public void logAfterDeleteJobApplicationById(String id) {
        log.info("ReactiveRecruitmentHelper | Recruiter deleted job application [id = {}]", id);
    }

    @Before("saveJobApplication()")
    public void logBeforeSaveJobApplication() {
        log.info("ReactiveRecruitmentHelper | Recruiter attempts to save job application");
    }

    @AfterReturning("saveJobApplication()")
    public void logAfterSaveJobApplication() {
        log.info("ReactiveRecruitmentHelper | Recruiter saved job application");
    }
}

