package com.reactiverecruitmenthelper.application;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Document
@ToString
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JobApplication {

    @Id
    private ObjectId _id;

    @Size(min = 2, max = 60)
    private String firstName;

    @Size(min = 2, max = 80)
    private String lastName;

    @Size(min = 2, max = 60)
    private String jobPosition;

    @Min(0)
    private int experienceYearsInJobPosition;

    @Size(max = 200)
    private String candidateMessage;

    @NotEmpty
    private String cvFilePath;

    private LocalDate applicationDate;

    private List<Technology> technologies;
}
