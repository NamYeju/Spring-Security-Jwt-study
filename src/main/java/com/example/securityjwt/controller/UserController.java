package com.example.securityjwt.controller;

import com.example.securityjwt.dto.LoginDto;
import com.example.securityjwt.dto.SignUpDto;
import com.example.securityjwt.entity.User;
import com.example.securityjwt.repository.UserRepository;
import com.example.securityjwt.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    // 회원가입
    @PostMapping("/join")
    public User join(@RequestBody SignUpDto signUpDto){
        return userRepository.save(User.builder()
                .identity(signUpDto.getIdentity())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .roles(Collections.singletonList("ROLE_USER"))
                .build());
    }

    // 로그인
    @PostMapping("/login")
    public String login(@RequestBody LoginDto loginDto){
        User user = userRepository.findByIdentity(loginDto.getIdentity())
                .orElseThrow( () -> new IllegalArgumentException("가입되지 않은 사용자입니다."));
        if(!passwordEncoder.matches(loginDto.getPassword(), user.getPassword()))
            throw new IllegalStateException("잘못된 비밀번호 입니다. ");

        return jwtTokenProvider.createToken(user.getIdentity(), user.getRoles());
    }

}
