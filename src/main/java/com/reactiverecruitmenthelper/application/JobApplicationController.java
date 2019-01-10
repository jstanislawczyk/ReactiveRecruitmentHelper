package com.reactiverecruitmenthelper.application;

import com.reactiverecruitmenthelper.helper.Page;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.reactiverecruitmenthelper.helper.Page.DEFAULT_PAGE_SIZE;
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

    @GetMapping("/all")
    @ResponseStatus(OK)
    public Flux<JobApplicationDto> getAllJobApplications() {
        return jobApplicationService
                .getAllJobApplications()
                .flatMap(jobApplication -> jobApplicationDtoConverter.jobApplicationMonoToDtoMono(Mono.just(jobApplication)));
    }

    @GetMapping
    @ResponseStatus(OK)
    public Mono<Page<JobApplicationDto>> getUsersPage(
            @RequestParam(name = "jobPosition", required = false) String jobPosition,
            @RequestParam(name = "experienceYears", required = false, defaultValue = "-1") String experienceYears,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = DEFAULT_PAGE_SIZE) int size
    ) {
        return jobApplicationService.getJobApplicationsPage(jobPosition, experienceYears, page, size);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Mono<JobApplicationDto> saveJobApplication(@RequestBody Mono<JobApplicationDto> jobApplicationMonoDto) {
        Mono<JobApplication> jobApplicationMono =
                jobApplicationService.saveJobApplication(jobApplicationDtoConverter.jobApplicationMonoFromDtoMono(jobApplicationMonoDto));

        return jobApplicationDtoConverter.jobApplicationMonoToDtoMono(jobApplicationMono);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public Mono<Void> deleteJobApplicationById(@PathVariable String id) {
        return jobApplicationService.deleteJobApplicationById(id);
    }
}