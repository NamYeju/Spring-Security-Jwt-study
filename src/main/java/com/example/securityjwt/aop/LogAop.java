package com.example.securityjwt.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@Aspect // 여러 공통 관점을 모듈화 한 것
@Component // 스프링 빈으로 등록하기 위해?
public class LogAop {

    // aspect를 적용할 target : 모든 패키지 내의 controller package에 존재하는 클래스
    // ProceedingJoinPoint :
    @Around("execution(* *..controller.*.*(..))")
    public Object logging(ProceedingJoinPoint pjp) throws Throwable{
        String params = getRequestParams();

        long startAt = System.currentTimeMillis();

        log.info("====== REQUEST :"+ "{"+pjp.getSignature().getDeclaringTypeName()+"}"
                +"({"+pjp.getSignature().getName() +"}) = {"+ params +"}");

        Object result = pjp.proceed();

        long endAt = System.currentTimeMillis();

        long time = endAt - startAt;
        log.info("====== RESPONSE :"+ "{"+pjp.getSignature().getDeclaringTypeName()+"}"
                +"({"+pjp.getSignature().getName() +"}) = {"+ result +"}" + time + "ms");


        return result;

    }
    // get requset value
    private String getRequestParams() {

        String params = "";

        RequestAttributes requestAttribute = RequestContextHolder.getRequestAttributes();

        if(requestAttribute != null){
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getRequest();
        }
        return params;
    }

}
