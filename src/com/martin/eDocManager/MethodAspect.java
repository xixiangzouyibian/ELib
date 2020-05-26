package com.martin.eDocManager;

import com.google.gson.Gson;
import com.martin.eDocManager.model.MethodDetails;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class MethodAspect {

    @Around("@annotation(MethodDetails)")
    public Object methodDetails(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        log.info(MethodDetails.builder().methodName(joinPoint.getSignature().getName())
                .methodArgs(new Gson().toJson(joinPoint.getArgs()).toString()).excutionTime(System.currentTimeMillis() - start).toString());
        return proceed;
    }

}
