
package com.example.jobportal.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ValidationAspect {
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(ValidationAspect.class);

	@Around("execution (* com.example.jobportal.service.JobService.getJob(..)) && args(postId)")
	public Object validateAndUpdate(ProceedingJoinPoint jp, int postId) throws Throwable {
		if (postId < 0) {
			log.info("postId is negative updating it");
			postId = -postId;
			log.info("New value, {}", postId);
		}

		Object obj = jp.proceed(new Object[] { postId });

		return obj;
	}

}
