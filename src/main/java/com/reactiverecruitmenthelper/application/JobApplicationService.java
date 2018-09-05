package com.reactiverecruitmenthelper.application;

import com.reactiverecruitmenthelper.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class JobApplicationService {

    private JobApplicationRepository jobApplicationRepository;

    Mono<JobApplication> getJobApplicationById(String id) {
        Mono<JobApplication> jobApplicationMono = jobApplicationRepository.findById(id);
        return jobApplicationMono.transform(mono -> throwErrorIfEmpty(jobApplicationMono, id));
    }

    Flux<JobApplication> getAllJobApplications() {
        return jobApplicationRepository.findAll();
    }

    Mono<JobApplication> saveJobApplication(Mono<JobApplication> jobApplicationMono) {
        return jobApplicationRepository
                .insert(jobApplicationMono)
                .flatMap(this::setTodayJobApplicationDate)
                .next();
    }

    Mono<Void> deleteJobApplicationById(String id) {
        return jobApplicationRepository.findById(id)
                .transform(mono -> throwErrorIfEmpty(mono, id))
                .flatMap(mono -> jobApplicationRepository.deleteById(id));
    }

    private Mono<JobApplication> setTodayJobApplicationDate(JobApplication jobApplication) {
        jobApplication.setApplicationDate(LocalDate.now());
        return Mono.just(jobApplication);
    }

    private Mono<JobApplication> throwErrorIfEmpty(Mono<JobApplication> source, String id) {
        return source.switchIfEmpty(Mono.error(new NotFoundException("Job application not found [_id = " + id + "]")));
    }
}
