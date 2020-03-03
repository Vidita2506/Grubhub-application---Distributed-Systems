package edu.sjsu.cmpe275.aop.tweet.aspect;

import java.io.IOException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.aspectj.lang.annotation.Around;

@Aspect
@Order(1)

/**
 * Aspect to retry the tweet operations that fail due to N/W errors
 */
public class RetryAspect {

	@Around("execution(public void edu.sjsu.cmpe275.aop.tweet.TweetService.*(..))")
	public void retry(ProceedingJoinPoint joinPoint) throws Throwable {
		for (int i = 0; (i <= 3) ; i++) {
			try {
				joinPoint.proceed();
				break;
			} catch (IllegalArgumentException ex) {
				throw ex;
			} catch (IOException ex) {
				if (i == 3) {
					System.out.println("Three retries failed");
					throw ex;
				} else {
					continue;
				}
			}
		}
	}
}
