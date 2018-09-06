package com.reactiverecruitmenthelper.user;

import com.reactiverecruitmenthelper.enums.Authority;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
@AllArgsConstructor
public class Role implements GrantedAuthority {

    private Authority authority;

    @Override
    public String getAuthority() {
        return authority.toString();
    }
}