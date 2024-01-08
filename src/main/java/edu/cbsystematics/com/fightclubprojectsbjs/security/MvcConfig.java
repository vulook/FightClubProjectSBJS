package edu.cbsystematics.com.fightclubprojectsbjs.security;

import org.springframework.context.annotation.Configuration;
<<<<<<< HEAD
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/registration").setViewName("registration");
        registry.addViewController("/server/all").setViewName("example/users-server");
        registry.addViewController("/internal/all").setViewName("example/users-internal");
        registry.addViewController("/internal/get-users").setViewName("example/get-users");
=======
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/registration").setViewName("registration");
>>>>>>> 1ce38dd (Initial commit)
    }

}

