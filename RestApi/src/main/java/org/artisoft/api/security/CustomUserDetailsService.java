package org.artisoft.api.security;

import org.artisoft.dal.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository users;

    public CustomUserDetailsService(UserRepository users) {

        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        org.artisoft.domain.User userInfo = this.users.getUserSecurityInfoByUserName(username);

        if(userInfo == null) {
            throw new UsernameNotFoundException("Username: " + username + " not found, tapilmadi....");
        }

        return userInfo;
    }
}
