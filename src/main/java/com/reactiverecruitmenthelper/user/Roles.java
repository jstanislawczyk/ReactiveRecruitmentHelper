package com.reactiverecruitmenthelper.user;

import lombok.*;

import javax.management.relation.Role;

@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@Data
public class Roles {
    private Role role;
}
