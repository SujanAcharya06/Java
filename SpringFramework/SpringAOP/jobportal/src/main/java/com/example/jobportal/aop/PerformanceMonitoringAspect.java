
package com.example.jobportal.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class PerformanceMonitoringAspect {

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(PerformanceMonitoringAspect.class);

	@Around("execution(* com.example.jobportal.service.JobService.*(..))")
	public Object monitorTime(ProceedingJoinPoint jp) throws Throwable {
		long start = System.currentTimeMillis();

		Object obj = jp.proceed();

		long end = System.currentTimeMillis();

		long timeTakenMs = (end - start);
		log.info("Time taken by {}, {} ms", jp.getSignature().getName(), timeTakenMs);

		return obj;
	}

}
