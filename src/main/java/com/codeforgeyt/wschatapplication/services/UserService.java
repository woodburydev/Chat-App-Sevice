package com.codeforgeyt.wschatapplication.services;

import com.codeforgeyt.wschatapplication.dto.RegisterRequest;
import com.codeforgeyt.wschatapplication.security.MyUserDetailsService;
import com.codeforgeyt.wschatapplication.dao.UserDao;
import com.codeforgeyt.wschatapplication.dto.AuthResponse;
import com.codeforgeyt.wschatapplication.entities.SafeUser;
import com.codeforgeyt.wschatapplication.entities.User;
import com.codeforgeyt.wschatapplication.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    MyUserDetailsService myUserDetailsService;

    @Autowired
    AuthenticationManager authManager;

    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    public SafeUser getSafeUserById(UUID id) {
        return userDao.getSafeUserById(id);
    }

    public SafeUser getSafeUserByUsername(String username) {
        return userDao.getSafeUserByUsername(username);
    }

    public SafeUser getLoggedInUser() {
        UserDetails myUsrDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (myUsrDetails == null) {
            return null;
        } else {
            User myUsr = getUserByUsername(myUsrDetails.getUsername());
            return new SafeUser(myUsr.getUsername(), myUsr.getId(), myUsr.getFirstName(), myUsr.getLastName(), myUsr.getEmail(), myUsr.getLastRequest());
        }
    }

    public ResponseEntity<?> handleAuthRequest(String username, String password) {
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        }
        catch (AuthenticationException e) {
            return ResponseEntity
                    .status(403)
                    .body("Invalid Username or Password");
        }
        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
        final String jwt = jwtUtil.generateToken(userDetails);
        return ResponseEntity
                .status(200)
                .body(new AuthResponse(jwt));
    }
    public int createNewUser(RegisterRequest registerRequest) {
        return userDao.insertNewUser(registerRequest);
    }

    public List<SafeUser> getAllUsers() {
        return userDao.getAllUsers();
    }



}
