package com.weatherapp.application.aspect;

import com.weatherapp.application.Constants;
import com.weatherapp.application.exception.ApiKeyNotFoundException;
import com.weatherapp.application.service.AccessMonitorService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Aspect
@Component
public class RateControlledAspect {

    private final AccessMonitorService accessMonitorService;
    private final HttpServletRequest httpServletRequest;

    public RateControlledAspect(AccessMonitorService accessMonitorService, HttpServletRequest httpServletRequest) {
        this.accessMonitorService = accessMonitorService;
        this.httpServletRequest = httpServletRequest;
    }

    @Around("@annotation(com.weatherapp.application.aspect.annotation.RateControlled)")
    public Object rateControlledExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String apiKey = extractApiKeyFromHeader();
        accessMonitorService.validateKey(apiKey);
        Object proceed = joinPoint.proceed();
        accessMonitorService.logKeyAccess(apiKey);
        return proceed;
    }

    private String extractApiKeyFromHeader() {
        return Optional.ofNullable(httpServletRequest.getHeader(Constants.Headers.API_KEY))
                .orElseThrow(() -> new ApiKeyNotFoundException(String.format(
                        "No %s header provided", Constants.Headers.API_KEY
                )));
    }

}
