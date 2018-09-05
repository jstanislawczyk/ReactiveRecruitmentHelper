package com.reactiverecruitmenthelper.application;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@ToString
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JobApplicationDto {

    private String _id;
    private String firstName;
    private String lastName;
    private String jobPosition;
    private int experienceYearsInJobPosition;
    private String candidateMessage;
    private String cvFilePath;
    private LocalDate applicationDate;
    private List<Technology> technologies;
}
