package com.reactiverecruitmenthelper.user;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.List;

@Document
@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@Data
public class User {

    @Id
    private String id;

    @Size(min = 3, max = 60)
    private String firstName;

    @Size(min = 3, max = 80)
    private String lastName;

    @Email
    private String email;

    @Size(min = 5, max = 60)
    private char[] password;

    private List<Roles> roles;
}
