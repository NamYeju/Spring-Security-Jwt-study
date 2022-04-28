package com.example.securityjwt.security.filter;

import com.example.securityjwt.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 스프링 시큐리티가 제공하는 filter 외에 custom된 필터를 생성하기 위해 servlet에서 제공하는 Filter 인터페이스를 상속하여 구현한다.
 * 이때 spring은 Filter에서 config 설정을 손쉽게 하기 위하여  GenericFilterBean 이라는 것을 제공한다.
 * Filter 인터페이스는 doFilter, init, Destroy 메소드 모두 구현해야하고, GenericFilterBean은 doFilter()만 구현하면 된다.
 * */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 헤더에서 JWT 를 받아오기
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        // 유효한 토큰인지 확인
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 토큰이 유효하면 토큰으로부터 유저 정보를 받아온다
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            // SecurityContext 에 Authentication 객체를 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }
}
