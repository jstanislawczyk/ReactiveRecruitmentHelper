package com.reactiverecruitmenthelper.application;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobApplicationRepository extends ReactiveMongoRepository<JobApplication, String> {
}
