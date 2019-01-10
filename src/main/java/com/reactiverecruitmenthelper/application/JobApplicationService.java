package com.reactiverecruitmenthelper.application;

import com.reactiverecruitmenthelper.exception.NotFoundException;
import com.reactiverecruitmenthelper.helper.Page;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class JobApplicationService {

    private JobApplicationRepository jobApplicationRepository;
    private JobApplicationDtoConverter jobApplicationDtoConverter;
    private JobApplicationDynamicQueryRepository jobApplicationDynamicQueryRepository;

    Mono<JobApplication> getJobApplicationById(String id) {
        Mono<JobApplication> jobApplicationMono = jobApplicationRepository.findById(id);
        return jobApplicationMono.transform(mono -> throwErrorIfEmpty(jobApplicationMono, id));
    }

    Flux<JobApplication> getAllJobApplications() {
        return jobApplicationRepository.findAll();
    }

    Mono<Page<JobApplicationDto>> getJobApplicationsPage(String jobPosition, String experienceYears, int page, int size) {
        JobApplication jobApplication = createJobApplication(jobPosition, experienceYears);

        return jobApplicationDynamicQueryRepository.findJobApplicationsByParams(jobApplication)
                .collectList()
                .map(usersList ->
                        new Page<>(
                                usersList
                                        .stream()
                                        .skip(page * size)
                                        .limit(size)
                                        .map(jobApplicationDtoConverter::jobApplicationToDto)
                                        .collect(Collectors.toList()),
                                page,
                                size,
                                usersList.size()
                        )
                );
    }

    Mono<JobApplication> saveJobApplication(Mono<JobApplication> jobApplicationMono) {
        return jobApplicationMono
                .flatMap(this::setTodayJobApplicationDate)
                .flatMap(jobApplicationRepository::save);
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

    private JobApplication createJobApplication(String jobPosition, String experienceYears) {
        return JobApplication.builder()
                    .jobPosition(jobPosition)
                    .experienceYearsInJobPosition(Integer.parseInt(experienceYears))
                .build();
    }
}
