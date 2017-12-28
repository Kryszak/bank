package bsr.project.bank.utility.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class AspectMethodLogger {

    @Around("execution(* *(..)) && @annotation(LogMethodCall)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        String methodName = point.getSignature().getName();

        log.info(">> {} called. Parameters: {}", methodName, point.getArgs());

        Object result = point.proceed();

        log.info("<< {} finished. Result: {}", methodName, result);

        return result;
    }
}
