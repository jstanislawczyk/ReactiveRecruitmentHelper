package com.reactiverecruitmenthelper.application;

import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@AllArgsConstructor
public class JobApplicationDynamicQueryRepository {

    private ReactiveMongoTemplate mongoTemplate;

    Flux<JobApplication> findJobApplicationsByParams(JobApplication jobApplication) {
        Query query = createQuery(jobApplication);
        return mongoTemplate.find(query, JobApplication.class);
    }

    private Query createQuery(JobApplication jobApplication) {
        Query query = new Query();

        if(jobApplication.getJobPosition() != null) {
            query.addCriteria(Criteria.where("jobPosition")
                    .is(jobApplication.getJobPosition()));
        }

        if(jobApplication.getFirstName() != null) {
            query.addCriteria(Criteria.where("firstName")
                    .is(jobApplication.getFirstName()));
        }

        if(jobApplication.getLastName() != null) {
            query.addCriteria(Criteria.where("lastName")
                    .is(jobApplication.getLastName()));
        }

        if(jobApplication.getExperienceYearsInJobPosition() > 0) {
            query.addCriteria(Criteria.where("experienceYearsInJobPosition")
                    .is(jobApplication.getExperienceYearsInJobPosition()));
        }

        return query;
    }
}
