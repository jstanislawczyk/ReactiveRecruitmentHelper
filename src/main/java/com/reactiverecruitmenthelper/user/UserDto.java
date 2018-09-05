package com.reactiverecruitmenthelper.user;

import com.reactiverecruitmenthelper.enums.Role;
import lombok.*;

import java.util.List;

@ToString
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
class UserDto {
    private String _id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<Role> roles;
}
