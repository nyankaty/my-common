package com.company.common.spring.aop;

import com.company.common.spring.aop.annotation.TimeTraceAspect;
import com.company.common.util.DateUtil;
import de.vandermeer.asciitable.AsciiTable;
import java.util.Date;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
@ConditionalOnProperty(
        value = {"app.time-trace-enabled"},
        havingValue = "true"
)
public class TimeTraceAOP {
    private static final Logger log = LoggerFactory.getLogger(TimeTraceAOP.class);

    public TimeTraceAOP() {
        // no arg constructor
    }

    @Pointcut("@annotation(timeTraceAspect)")
    public void pointcutAnnotationTimeTrace(TimeTraceAspect timeTraceAspect) {
        // empty
    }

    @Around("pointcutAnnotationTimeTrace(timeTraceAspect)")
    public Object aroundProcessAnnotation(ProceedingJoinPoint joinPoint, TimeTraceAspect timeTraceAspect) throws Throwable {
        long start = System.nanoTime();
        AsciiTable at = new AsciiTable();
        if (timeTraceAspect.enabled()) {
            this.addRow(at, "Name", "Value");
            this.addRow(at, "Class", joinPoint.getSignature().getDeclaringTypeName());
            this.addRow(at, "MethodName", joinPoint.getSignature().getName());
            this.addRow(at, "Start time ", DateUtil.formatDateLog(new Date()));
        }

        Object value = joinPoint.proceed();
        if (timeTraceAspect.enabled()) {
            this.addRow(at, "End time ", DateUtil.formatDateLog(new Date()));
            this.addRow(at, "Time process end Nano ", System.nanoTime() - start);
            at.addRule();

            String logTxt = at.render().trim();
            log.info("\n{}", logTxt);
        }

        return value;
    }

    private void addRow(AsciiTable at, Object... columns) {
        at.addRule();
        at.addRow(columns);
    }
}

