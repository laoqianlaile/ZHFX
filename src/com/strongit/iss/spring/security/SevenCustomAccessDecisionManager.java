package com.strongit.iss.spring.security;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 *
 * @author William
 */
public class SevenCustomAccessDecisionManager implements AccessDecisionManager {

    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> attributes) throws AccessDeniedException, InsufficientAuthenticationException {
        if (null == attributes){
            System.out.println("No attributes configed for current request.");
            return;
        }

        if(authentication.getPrincipal().equals("anonymousUser")){
            throw new InsufficientAuthenticationException("匿名用户访问受限");
        }

        for (ConfigAttribute attribute : attributes) {
            String needRole = ((SecurityConfig) attribute).getAttribute();
            // authority为用户所被赋予的权限, needRole 为访问相应的资源应该具有的权限。
            for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                if (needRole.equals(grantedAuthority.getAuthority())){
                    //System.out.println("Current request is permitted.");
                    return;
                }

            }
        }
        throw new AccessDeniedException("权限不足!");
    }

    @Override
    public boolean supports(ConfigAttribute ca) {
        return true;
    }

    @Override
    public boolean supports(Class<?> type) {
        return true;
    }
    
}
