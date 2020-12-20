package com.codeforgeyt.wschatapplication.config;

import com.codeforgeyt.wschatapplication.dao.UserDao;
import com.codeforgeyt.wschatapplication.filters.UserRequestTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    private UserDao userDao;

    @Autowired
    public AppConfig(UserDao userDao) {
        this.userDao = userDao;
    }

    @Bean
    public FilterRegistrationBean<UserRequestTimestamp> filterRegistrationBean() {
        FilterRegistrationBean<UserRequestTimestamp> registrationBean = new FilterRegistrationBean<UserRequestTimestamp>();
        UserRequestTimestamp userRequestTimestamp = new UserRequestTimestamp(userDao);
        registrationBean.setFilter(userRequestTimestamp);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(2);
        return registrationBean;
    }
}
