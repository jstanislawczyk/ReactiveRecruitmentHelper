package com.reactiverecruitmenthelper.application;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/jobs")
@AllArgsConstructor
public class JobApplicationController {

    private JobApplicationService jobApplicationService;
    private JobApplicationDtoConverter jobApplicationDtoConverter;

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public Mono<JobApplicationDto> getJobApplicationById(@PathVariable String id) {
        return jobApplicationDtoConverter.jobApplicationMonoToDtoMono(jobApplicationService.getJobApplicationById(id));
    }

    @GetMapping
    @ResponseStatus(OK)
    public Flux<JobApplicationDto> getAllJobApplications() {
        return jobApplicationService.getAllJobApplications().flatMap(jobApplication -> jobApplicationDtoConverter.jobApplicationMonoToDtoMono(Mono.just(jobApplication)));
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Mono<JobApplicationDto> saveJobApplication(@RequestBody Mono<JobApplicationDto> jobApplicationMonoDto) {
        Mono<JobApplication> jobApplicationMono = jobApplicationService.saveJobApplication(jobApplicationDtoConverter.jobApplicationMonoFromDtoMono(jobApplicationMonoDto));
        return jobApplicationDtoConverter.jobApplicationMonoToDtoMono(jobApplicationMono);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public Mono<Void> deleteJobApplicationById(@PathVariable String id) {
        return jobApplicationService.deleteJobApplicationById(id);
    }
}