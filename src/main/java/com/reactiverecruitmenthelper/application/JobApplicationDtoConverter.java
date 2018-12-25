package com.reactiverecruitmenthelper.application;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class JobApplicationDtoConverter {
    Mono<JobApplicationDto> jobApplicationMonoToDtoMono(Mono<JobApplication> jobApplicationMono) {
        return jobApplicationMono.flatMap(jobApplication ->
                Mono.just(JobApplicationDto.builder()
                        ._id(jobApplication.get_id())
                        .firstName(jobApplication.getFirstName())
                        .lastName(jobApplication.getLastName())
                        .jobPosition(jobApplication.getJobPosition())
                        .experienceYearsInJobPosition(jobApplication.getExperienceYearsInJobPosition())
                        .candidateMessage(jobApplication.getCandidateMessage())
                        .cvFilePath(jobApplication.getCvFilePath())
                        .applicationDate(jobApplication.getApplicationDate())
                        .technologies(jobApplication.getTechnologies())
                        .build()
                ));
    }

    Mono<JobApplication> jobApplicationMonoFromDtoMono(Mono<JobApplicationDto> jobApplicationDtoMono) {
        return jobApplicationDtoMono.flatMap(jobApplicationDto ->
                Mono.just(JobApplication.builder()
                        ._id(jobApplicationDto.get_id())
                        .firstName(jobApplicationDto.getFirstName())
                        .lastName(jobApplicationDto.getLastName())
                        .jobPosition(jobApplicationDto.getJobPosition())
                        .experienceYearsInJobPosition(jobApplicationDto.getExperienceYearsInJobPosition())
                        .candidateMessage(jobApplicationDto.getCandidateMessage())
                        .cvFilePath(jobApplicationDto.getCvFilePath())
                        .applicationDate(jobApplicationDto.getApplicationDate())
                        .technologies(jobApplicationDto.getTechnologies())
                        .build()
                ));
    }

    JobApplicationDto jobApplicationToDto(JobApplication jobApplication) {
        return JobApplicationDto
                .builder()
                ._id(jobApplication.get_id())
                .firstName(jobApplication.getFirstName())
                .lastName(jobApplication.getLastName())
                .jobPosition(jobApplication.getJobPosition())
                .experienceYearsInJobPosition(jobApplication.getExperienceYearsInJobPosition())
                .candidateMessage(jobApplication.getCandidateMessage())
                .cvFilePath(jobApplication.getCvFilePath())
                .applicationDate(jobApplication.getApplicationDate())
                .technologies(jobApplication.getTechnologies())
                .build();

    }
}
