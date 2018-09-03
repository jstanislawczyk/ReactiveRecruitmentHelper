package com.reactiverecruitmenthelper.user;

import com.reactiverecruitmenthelper.enums.Role;
import lombok.*;

@ToString
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Roles {
    private Role role;
}
