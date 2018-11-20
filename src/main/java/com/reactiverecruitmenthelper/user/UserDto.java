package com.reactiverecruitmenthelper.user;

import lombok.*;

import java.util.List;

@ToString
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
    private String _id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<Role> roles;
    private boolean active = true;
}
