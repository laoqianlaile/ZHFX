package com.strongit.iss.spring.security;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 *
 * @author LvLL
 */
public class SevenCustomUserDetailsService implements UserDetailsService {

    public SevenCustomUserDetailsService(){

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if(username.equalsIgnoreCase("ndrcsic")){
            return new User("ndrcsic", "ndrcsic", true, true, true, true, AuthorityUtils.createAuthorityList("USER","ndrcsic"));
        } else if(username.equalsIgnoreCase("user")){
            return new User("user", "123", true, true, true, true, AuthorityUtils.createAuthorityList("USER"));
        }

        throw new UsernameNotFoundException("Can't find " + username);
    }
    
}
