package edu.sjsu.cmpe275.lab1.aspect;

import java.io.IOException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class RetryAspect {
	boolean flag = true;

	@Around("execution(public void edu.sjsu.cmpe275.lab1.TweetService.tweet(..))")
	public void retry(ProceedingJoinPoint joinPoint) throws Throwable {
		for (int i = 0; (i <= 3) && (flag = true); i++) {
			try {
				joinPoint.proceed();
				flag = false;
				if (!flag) {
					break;
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				break;
			} catch (IOException x) {
				if (i == 3) {
					x.printStackTrace();
					retrycompleted = true;
					break;
				} else {
					continue;
				}
			}
		}
	}
}
