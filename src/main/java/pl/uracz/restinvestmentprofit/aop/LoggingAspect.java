package pl.uracz.restinvestmentprofit.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
@EnableAspectJAutoProxy
@Slf4j
public class LoggingAspect {

    @Autowired(required = false)
    private HttpServletRequest httpServletRequest;

    @Pointcut("execution(* pl.uracz.restinvestmentprofit.controller..*(..))")
    private void anyPublicMethod() {
    }

    @Pointcut("execution(* pl.uracz.restinvestmentprofit.exception.CustomGlobalExceptionHandler..*(..))")
    private void anyCustomGlobalExceptionHandlerMethod() {
    }

    @After(value = "anyPublicMethod()")
    public void afterControllerMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        ResponseStatus status = method.getAnnotation(ResponseStatus.class);
        log.info("Http call method: " + httpServletRequest.getMethod() + ", url: " + httpServletRequest.getRequestURI() + ", status code: " + status.value());
    }

    @AfterReturning(value = "anyCustomGlobalExceptionHandlerMethod()", returning = "returnValue")
    public void afterCustomGlobalExceptionHandlerMethod(Object returnValue){
        ResponseEntity<?> responseEntity = (ResponseEntity<?>) returnValue;
        log.error("Http call method: " + httpServletRequest.getMethod() + ", url: " + httpServletRequest.getRequestURI() + ", status code: " + responseEntity.getStatusCode());
    }
}
