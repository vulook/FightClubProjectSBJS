package edu.cbsystematics.com.fightclubprojectsbjs.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cbsystematics.com.fightclubprojectsbjs.internal.AnotherEndpointsFilter;
import edu.cbsystematics.com.fightclubprojectsbjs.repository.UserRepository;
import edu.cbsystematics.com.fightclubprojectsbjs.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.web.client.RestTemplate;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserRepository userRepository;

    private final SuccessUserHandler successUserHandler;

    @Autowired
    public WebSecurityConfig(UserRepository userRepository, SuccessUserHandler successUserHandler) {
        this.userRepository = userRepository;
        this.successUserHandler = successUserHandler;
    }

    @Override
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(userRepository);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(
                        "/registration",
                        "/verify",
                        "/server/**",
                        "/internal/**").permitAll()
                .antMatchers(
                        "/css/**",
                        "/img/**",
                        "/webjars/**").permitAll()
                .antMatchers("/fight-club/admin/**").hasAuthority("ADMIN")
                .antMatchers("/fight-club/user/**").hasAnyAuthority("ADMIN", "USER")
                .antMatchers("/fight-club").authenticated()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").successHandler(successUserHandler)
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }


    @Value("${server.anotherPort}")
    private int anotherPort;

    @Value("${server.anotherPathPrefix}")
    private String anotherPathPrefix;

    @Bean
    public FilterRegistrationBean<AnotherEndpointsFilter> trustedEndpointsFilter() {
        return new FilterRegistrationBean<>(new AnotherEndpointsFilter(anotherPort, anotherPathPrefix));
    }

    @Bean
    RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(new ObjectMapper());
        restTemplate.getMessageConverters().add(converter);
        return restTemplate;
    }


}