package com.example.jobportal.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspect {

	private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

	// return type, fullyQualifiedClassName.methodName(args)

	@Before("execution(* com.example.jobportal.service.JobService.getAllJobs(..)) || execution(* com.example.jobportal.service.JobService.getJob(..))")
	public void logMethodCalled(JoinPoint jp) {
		log.info("@Before - Method called {}", jp.getSignature().getName());
	}

	@After("execution(* com.example.jobportal.service.JobService.getAllJobs(..)) || execution(* com.example.jobportal.service.JobService.getJob(..))")
	public void logMethodExecuted(JoinPoint jp) {
		log.info("@After - Method Executed {}", jp.getSignature().getName());
	}

	@AfterThrowing("execution(* com.example.jobportal.service.JobService.getAllJobs(..)) || execution(* com.example.jobportal.service.JobService.getJob(..))")
	public void logMethodCrash(JoinPoint jp) {
		log.info("@AfterThrowing - Method has some issues {}", jp.getSignature().getName());
	}

	@AfterReturning("execution(* com.example.jobportal.service.JobService.getAllJobs(..)) || execution(* com.example.jobportal.service.JobService.getJob(..))")
	public void logMethodExecutedSuccess(JoinPoint jp) {
		log.info("@AfterReturning - Method Executed Successfully{}", jp.getSignature().getName());
	}
}
