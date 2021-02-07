package pl.uracz.restinvestmentprofit.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
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

    @Autowired(required = false)
    private HttpServletResponse httpServletResponse;

    @Pointcut("execution(* pl.uracz.restinvestmentprofit.controller..*(..))")
    private void anyPublicMethod() {
    }

    @Around("anyPublicMethod()")
    public void afterControllerMethod(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RequestMapping[] reqMappingAnnotations;

        Annotation[] annos = method.getDeclaredAnnotations();
        String methodName = null;
        for (Annotation anno : annos) {
            if (anno.annotationType()
                    .isAnnotationPresent(org.springframework.web.bind.annotation.RequestMapping.class)) {
                reqMappingAnnotations = anno.annotationType()
                        .getAnnotationsByType(org.springframework.web.bind.annotation.RequestMapping.class);
                for (RequestMapping annotation : reqMappingAnnotations) {
                    for (RequestMethod reqMethod : annotation.method()) {
                        methodName = reqMethod.name();
                    }
                }
            }
        }
        log.info("Http call method: " + methodName + ", url: " + httpServletRequest.getRequestURI() + ", status code: " + httpServletResponse.getStatus());
    }
}
