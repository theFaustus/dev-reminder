package com.evil.devreminder.aop.advice;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class ExceptionLoggingAspect {

    @AfterThrowing(pointcut = "com.evil.devreminder.aop.SystemArchitecturePointcut.repository() " +
            "|| com.evil.devreminder.aop.SystemArchitecturePointcut.service()", throwing = "ex")
    public void logException(Exception ex){
        log.error("Something wrong happened ", ex);
    }
}
