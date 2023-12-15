package com.ll.medium.global.security;

import com.ll.medium.global.exceptionHandler.CustomAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/gen/**")
                                .permitAll()
                                .requestMatchers("/resource/**")
                                .permitAll()
                                .requestMatchers("/h2-console/**")
                                .permitAll()
                                .requestMatchers("/admin")
                                .hasRole("ADMIN")
                                .anyRequest()
                                .permitAll()
                )
                .headers(
                        headers ->
                                headers.frameOptions(
                                        frameOptions ->
                                                frameOptions.sameOrigin()
                                )
                )
                .csrf(
                        csrf ->
                                csrf.ignoringRequestMatchers(
                                        "/h2-console/**"
                                )
                )
                .exceptionHandling(
                        exceptionHandling ->
                                exceptionHandling.accessDeniedHandler(new CustomAccessDeniedHandler())  // AccessDeniedHandler를 설정합니다.
                )
                .formLogin(
                        (formLogin) ->
                                formLogin
                                        .loginPage("/member/login")
                                        .defaultSuccessUrl("/?msg=" + URLEncoder.encode("환영합니다.", StandardCharsets.UTF_8))
                                        .failureUrl("/member/login?failMsg=" + URLEncoder.encode("아이디 또는 비밀번호가 일치하지 않습니다.",StandardCharsets.UTF_8))
                )
                .logout(
                        logout -> logout.logoutRequestMatcher(
                                new AntPathRequestMatcher("/member/logout"))
                );

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;
}
