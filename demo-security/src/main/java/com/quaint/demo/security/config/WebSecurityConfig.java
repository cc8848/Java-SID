package com.quaint.demo.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 *
 * @author quaint
 * @date 2020-01-12 18:35
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // 授权
            .authorizeRequests()
                .antMatchers("/login").permitAll()
                // 资源 避免重复写, 否则该资源的角色,下面的会覆盖上面的.
                .antMatchers("/user").hasAnyRole("USER","ADMIN")
                .antMatchers("/system").hasRole("ADMIN")
                // 上面把资源写完, 授权一次就好了, 授权多次可能会出现覆盖问题.
                .anyRequest().authenticated()
                .and()
            // 登录方式
            .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/index")
                .permitAll()
                .and()
            // 登出
            .logout()
                .permitAll();
    }

    @Override
    public void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.inMemoryAuthentication()
                // 必须要指定 密码加密方式 , 否则会抛出异常 There is no PasswordEncoder mapped for the id "null"
                .passwordEncoder(new BCryptPasswordEncoder())
                .withUser("quaint")
                .password(new BCryptPasswordEncoder().encode("quaint")).roles("USER")
                .and()
                .withUser("admin")
                .password(new BCryptPasswordEncoder().encode("admin")).roles("ADMIN")
        ;
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("password")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }



}
