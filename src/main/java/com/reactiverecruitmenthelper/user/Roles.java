package com.reactiverecruitmenthelper.user;

import com.reactiverecruitmenthelper.enums.Role;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

@ToString
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
class Roles {

    @Id
    private String id;

    private Role role;

    @DBRef(lazy = true)
    private List<User> users;
}
