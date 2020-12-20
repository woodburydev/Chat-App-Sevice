package com.codeforgeyt.wschatapplication.filters;

import com.codeforgeyt.wschatapplication.dao.UserDao;
import com.codeforgeyt.wschatapplication.entities.SafeUser;
import com.codeforgeyt.wschatapplication.entities.User;
import com.codeforgeyt.wschatapplication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.UUID;

@Repository
public class UserRequestTimestamp extends OncePerRequestFilter {

    private UserDao userDao;

    public UserRequestTimestamp(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        Authentication myUsrDetails = SecurityContextHolder.getContext().getAuthentication();
        if (myUsrDetails != null) {
            String name = myUsrDetails.getName();
                if (!name.equals("anonymousUser")) {
                    // Go into the database and set the last known action to timestamp now.
                    if (userDao != null) {
                        userDao.updateLastRequest(name);
                    }
                }
            }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
