package com.example.securityjwt.config;

import com.example.securityjwt.repository.UserRepository;
import com.example.securityjwt.security.filter.JwtAuthenticationFilter;
import com.example.securityjwt.security.jwt.JwtTokenProvider;
import com.example.securityjwt.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
/**
 * 스프링 시큐리티가 사용자를 인증하는 방법이 담긴 객체 configure
 * */
/**
 * @EnableWebSecurity
 * 이 어노테이션을 통해 웹 보안을 활성화
 * 하지만 그자체로는 유용하지 않고, 스프링 시큐리티가 WebSecurityConfigurer를 구현하거나
 * 컨텍스트의 WebSebSecurityConfigurerAdapter를 확장한 빈으로 설정되어 있어야 한다.
 * */
@RequiredArgsConstructor
@EnableWebSecurity // WebSecurityConfigurerAdapter를 상속받은 클래스에 이 어노테이션을 붙임으로써 SpringSecurityFilterChain 포함
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    // 정적 자원에 대해서는 security 설정을 적용하지 않음
//    @Override
//    public void configure(WebSecurity web){
//        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
//    }
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(new CustomUserDetailService(userRepository)).passwordEncoder(passwordEncoder());
//    }

    // 암호화에 필요한 PasswordEncoder 를 Bean 등록합니다.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // authenticationManager를 Bean 등록합니다.
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception{
        http
                .httpBasic().disable() // rest api 만을 고려하여 기본 설정은 해제
                .csrf().disable() // csrf 보안 토큰 disable처리.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰 기반 인증이므로 세션 사용x
                .and()
                .authorizeRequests() // 요청에 대한 사용권한 체크
                .anyRequest().permitAll() // 그외 나머지 요청은 누구나 접근 가능
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class);
        // JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 전에 넣는다
    }




}
