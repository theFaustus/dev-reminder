package com.evil.devreminder.aop.advice;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
@Slf4j
public class TracingLoggingAspect {

    @Before("com.evil.devreminder.aop.SystemArchitecturePointcut.repository()")
    public void traceRepositoryBefore(JoinPoint joinPoint) {
        log.info("entering repo : " + joinPoint.getStaticPart().getSignature() + " with " + Arrays.toString(joinPoint.getArgs()));
    }

    @After("com.evil.devreminder.aop.SystemArchitecturePointcut.repository()")
    public void traceRepositoryAfter(JoinPoint joinPoint) {
        log.info("exiting repo : " + joinPoint.getStaticPart().getSignature() + " with " + Arrays.toString(joinPoint.getArgs()));
    }

    @Before("com.evil.devreminder.aop.SystemArchitecturePointcut.service()")
    public void traceServiceBefore(JoinPoint joinPoint) {
        log.info("entering service : " + joinPoint.getStaticPart().getSignature() + " with " + Arrays.toString(joinPoint.getArgs()));
    }

    @After("com.evil.devreminder.aop.SystemArchitecturePointcut.service()")
    public void traceServiceAfter(JoinPoint joinPoint) {
        log.info("exiting service : " + joinPoint.getStaticPart().getSignature() + " with " + Arrays.toString(joinPoint.getArgs()));
    }
}
