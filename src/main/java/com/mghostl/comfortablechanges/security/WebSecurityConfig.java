package com.mghostl.comfortablechanges.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration(value = "securityConfig")
@EnableWebSecurity
@PropertySource(value = "classpath:secrets.properties")
@ConfigurationProperties(prefix = "security")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Setter
    @Getter
    private String password;

    @Setter
    @Getter
    private String user;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/rates", "/currencies", "/from", "/to").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .and()
                .logout()
                    .permitAll();
    }

    @Bean
    @DependsOn(value = "securityConfig")
    @Override
    public UserDetailsService userDetailsService() {

        // TODO change encoder, separate credentials in properties file, encode password
        return new InMemoryUserDetailsManager(User.withDefaultPasswordEncoder()
                .username(this.user)
                .password(this.password)
                .roles("USER")
                .build());
    }
}