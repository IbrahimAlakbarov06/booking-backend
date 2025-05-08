package turing.edu.az.booking.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* turing.edu.az.booking.controller..*(..))")
    public void controllerLayer() {}

    @Before("controllerLayer()")
    public void logControllerBefore(JoinPoint jp) {
        log.info("→ [CONTROLLER] Entering {}.{}() with args = {}",
                jp.getSignature().getDeclaringType().getSimpleName(),
                jp.getSignature().getName(),
                jp.getArgs());
    }

    @AfterReturning(pointcut = "controllerLayer()", returning = "res")
    public void logControllerAfter(JoinPoint jp, Object res) {
        log.info("← [CONTROLLER] Exiting {}.{}() with response = {}",
                jp.getSignature().getDeclaringType().getSimpleName(),
                jp.getSignature().getName(),
                res);
    }

    @AfterThrowing(pointcut = "controllerLayer()", throwing = "ex")
    public void logControllerAfterThrowing(JoinPoint jp, Throwable ex) {
        log.error("⚠ [CONTROLLER] Exception in {}.{}(): {}",
                jp.getSignature().getDeclaringType().getSimpleName(),
                jp.getSignature().getName(),
                ex.getMessage(), ex);
    }
}
