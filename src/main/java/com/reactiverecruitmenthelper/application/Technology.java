package com.reactiverecruitmenthelper.application;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@Data
public class Technology {

    @Size(min = 2, max = 30)
    private String name;

    @Min(1)
    @Max(10)
    private int knowledgeLevel;
}
