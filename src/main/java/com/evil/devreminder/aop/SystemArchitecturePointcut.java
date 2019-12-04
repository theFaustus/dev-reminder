package com.evil.devreminder.aop;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

public class SystemArchitecturePointcut {
    @Pointcut("execution(* com.evil.devreminder.repository.*.*(..))")
    public void repository(){}

    @Pointcut("execution(* com.evil.devreminder.service.*.*(..))")
    public void service(){}
}
