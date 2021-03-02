package pl.uracz.restinvestmentprofit.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
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

    @AfterReturning(value = "anyPublicMethod()", returning = "resultValue")
    public void afterControllerMethod(JoinPoint joinPoint, Object resultValue) {
        String methodName = getString(joinPoint);
        ResponseEntity<?> result = (ResponseEntity<?>) resultValue;
        log.info("Http call method: " + methodName + ", url: " + httpServletRequest.getRequestURI() + ", status code: " + result.getStatusCode().value());
    }

    private String getString(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RequestMapping[] reqMappingAnnotations;
        Annotation[] annotations = method.getDeclaredAnnotations();
        String methodName = null;
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().isAnnotationPresent(RequestMapping.class)) {
                reqMappingAnnotations = annotation.annotationType().getAnnotationsByType(RequestMapping.class);
                for (RequestMapping requestMapping : reqMappingAnnotations) {
                    for (RequestMethod reqMethod : requestMapping.method()) {
                        methodName = reqMethod.name();
                    }
                }
            }
        }
        return methodName;
    }
}
