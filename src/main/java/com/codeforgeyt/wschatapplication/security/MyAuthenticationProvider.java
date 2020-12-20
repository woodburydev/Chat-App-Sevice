package com.codeforgeyt.wschatapplication.security;

import com.codeforgeyt.wschatapplication.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class MyAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDao userDao;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final UsernamePasswordAuthenticationToken upAuth = (UsernamePasswordAuthenticationToken) authentication;
        final String username = (String) authentication.getPrincipal();
        final String password = (String) upAuth.getCredentials();
        final String storedPassword = userDao.getUserByUsername(username).getPassword();
        if (!BCrypt.checkpw(password, storedPassword)) {
            throw new BadCredentialsException("Not the correct password");
        }
        final Object principal = authentication.getPrincipal();
        final UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(
                principal, authentication.getCredentials(),
                Collections.emptyList());
        result.setDetails(authentication.getDetails());

        return result;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
