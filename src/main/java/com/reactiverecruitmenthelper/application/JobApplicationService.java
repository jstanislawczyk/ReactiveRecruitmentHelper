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

    Mono<JobApplication> getJobApplicationById(String id) {
        Mono<JobApplication> jobApplicationMono = jobApplicationRepository.findById(id);
        return jobApplicationMono.transform(mono -> throwErrorIfEmpty(jobApplicationMono, id));
    }

    Flux<JobApplication> getAllJobApplications() {
        return jobApplicationRepository.findAll();
    }

    Mono<Page<JobApplicationDto>> getJobApplicationsPage(int page, int size) {
        return jobApplicationRepository.findAll()
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
}
