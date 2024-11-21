package com.emi.onlineshop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("within(com.emi.onlineshop.controllers.CartItemController)")
    public void executeLoggingForCartItemController() {
    }

    @Pointcut("execution(* delete*(..))")
    public void executeLoggingForMethodsWithDelete() {
    }

    @Pointcut("execution(* *Aop*(..))")
    public void executeLoggingForMethodWithAopInHelloController() {
    }

    @Pointcut("within(com.emi.onlineshop.controllers..*)")
    public void executeLoggingForAllMethodsInControllers() {
    }

    @Before("executeLoggingForCartItemController()")
    public void logMethodCall(JoinPoint joinPoint) {
        StringBuilder message = new StringBuilder("Method: ");
        message.append(joinPoint.getSignature().getName());

        Object[] args = joinPoint.getArgs();
        if (null != args && args.length > 0) {
            message.append(" args: [ | ");
            Arrays.stream(args).forEach(arg -> message.append(arg).append(" | "));
            message.append("]");
        }

        LOGGER.info(message.toString());
    }

    @Before("executeLoggingForMethodsWithDelete()")
    public void logMethodCall2(JoinPoint joinPoint) {
        LOGGER.info("logMethodCall2");
        logMethodCall(joinPoint);
    }

    @After("executeLoggingForMethodWithAopInHelloController()")
    public void logMethodCall3(JoinPoint joinPoint) {
        LOGGER.info("log After");
        logMethodCall(joinPoint);
    }

    @AfterReturning("executeLoggingForMethodWithAopInHelloController()")
    public void logMethodCall4(JoinPoint joinPoint) {
        LOGGER.info("log After returning");
        logMethodCall(joinPoint);
    }

    @AfterThrowing("executeLoggingForMethodWithAopInHelloController()")
    public void logMethodCall5(JoinPoint joinPoint) {
        LOGGER.info("log After Throwing");
        logMethodCall(joinPoint);
    }

    @Before("executeLoggingForAllMethodsInControllers()")
    public void logInfoBefore(JoinPoint joinPoint) {
        LOGGER.info("***** BEFORE *****");
    }

    @After("executeLoggingForAllMethodsInControllers()")
    public void logInfoAfter(JoinPoint joinPoint) {
        LOGGER.info("***** AFTER *****");
    }

}
