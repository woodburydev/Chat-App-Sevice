package com.codeforgeyt.wschatapplication.controllers;

import com.codeforgeyt.wschatapplication.dto.AuthRequest;
import com.codeforgeyt.wschatapplication.dto.RegisterRequest;
import com.codeforgeyt.wschatapplication.entities.SafeUser;
import com.codeforgeyt.wschatapplication.entities.User;
import com.codeforgeyt.wschatapplication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthRequest authRequest) {
        return userService.handleAuthRequest(authRequest.getUsername(), authRequest.getPassword());
    }

    @GetMapping(value = "/loggedInUser", produces = "application/json")
    public SafeUser getLoggedInUser() {
        return userService.getLoggedInUser();
    }

    @PostMapping("/register")
    public int createNewUser(@RequestBody RegisterRequest registerRequest) {
        return userService.createNewUser(registerRequest);
    }

    @GetMapping(value = "/getAllUsers", produces = "application/json")
    public List<SafeUser> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(value = "/getUserByUsername", produces = "application/json")
    public SafeUser getUserByUsername(@RequestParam String username) {
        return userService.getSafeUserByUsername(username);
    }

    @GetMapping(value = "/isUserOnline", produces = "application/json")
    public boolean isUserOnline(@RequestParam UUID id) {
        SafeUser requestsUser = userService.getSafeUserById(id);
        Instant now = Instant.now();
        Duration between = Duration.between(requestsUser.getLastRequest().toInstant(), now);
        int timeInSecondsAgo = Integer.parseInt(String.valueOf(between.getSeconds()));
        return timeInSecondsAgo < 600;
    }
}
