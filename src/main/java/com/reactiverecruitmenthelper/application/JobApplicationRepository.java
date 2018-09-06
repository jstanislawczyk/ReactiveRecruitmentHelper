package com.reactiverecruitmenthelper.application;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface JobApplicationRepository extends ReactiveMongoRepository<JobApplication, String> {
}
