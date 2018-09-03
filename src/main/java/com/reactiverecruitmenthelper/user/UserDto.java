package com.reactiverecruitmenthelper.user;

import lombok.*;

import java.util.List;

@ToString
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
class UserDto {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private char[] password;
    private List<Roles> roles;
}
