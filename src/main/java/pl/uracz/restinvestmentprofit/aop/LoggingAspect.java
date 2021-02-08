package pl.uracz.restinvestmentprofit.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
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

    @After("anyPublicMethod()")
    public void afterControllerMethod(JoinPoint joinPoint) {
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
        log.info("Http call method: " + methodName + ", url: " + httpServletRequest.getRequestURI() + ", status code: " + httpServletResponse.getStatus());
    }
}
