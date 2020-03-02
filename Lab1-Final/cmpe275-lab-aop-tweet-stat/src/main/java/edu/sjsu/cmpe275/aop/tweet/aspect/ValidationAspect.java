package edu.sjsu.cmpe275.aop.tweet.aspect;

import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import edu.sjsu.cmpe275.aop.tweet.TweetStatsServiceImpl;

@Aspect
@Order(2)
public class ValidationAspect {
	
	@Autowired
	TweetStatsServiceImpl stats;
	
	@Before("execution(public void edu.sjsu.cmpe275.aop.tweet.TweetService.tweet(..))")
	public void beforeTweet(JoinPoint joinPoint) {
		String user = (String) joinPoint.getArgs()[0];
		String msg = (String) joinPoint.getArgs()[1];
		if (isEmptyString(user) || isEmptyString(msg) || msg.length() > 140) {
			throw new IllegalArgumentException();
		}
	}
	
	@Before("execution(public void edu.sjsu.cmpe275.aop.tweet.TweetService.follow(..))")
	public void beforeFollow(JoinPoint joinPoint) {
		String follower = (String) joinPoint.getArgs()[0];
		String followee = (String) joinPoint.getArgs()[1];
		if (isEmptyString(follower) || isEmptyString(followee)) {
			throw new IllegalArgumentException("Ivalid arguments");
		} else if (followee.equals(follower)) {
			throw new UnsupportedOperationException("You cannot follow yourself");
		}
	}
	
	@Before("execution(public void edu.sjsu.cmpe275.aop.tweet.TweetService.block(..))")
	public void beforeBlock(JoinPoint joinPoint) {
		String user = (String) joinPoint.getArgs()[0];
		String follower = (String) joinPoint.getArgs()[1];
		if (isEmptyString(user) || isEmptyString(follower)) {
			throw new IllegalArgumentException("Ivalid arguments");
		} else if (user.equals(follower)) {
			throw new UnsupportedOperationException("You cannot block yourself");
		}
	}
	
	@Before("execution(public void edu.sjsu.cmpe275.aop.tweet.TweetService.unblock(..))")
	public void beforeUnBlock(JoinPoint joinPoint) {
		String user = (String) joinPoint.getArgs()[0];
		String follower = (String) joinPoint.getArgs()[1];
		if (isEmptyString(user) || isEmptyString(follower)) {
			throw new IllegalArgumentException("Ivalid arguments");
		} else if (user.equals(follower)) {
			throw new UnsupportedOperationException("You cannot unblock yourself");
		} else if ( isListEmpty(stats.getUserBlockedFollowersListMap().get(user))
			|| (!stats.getUserBlockedFollowersListMap().get(user).contains(follower))) {
			
			throw new UnsupportedOperationException("This user is not blocked");
		}
	}
	
	private boolean isEmptyString(String data) {
		if (data == null || data.isEmpty()) {
			return true;
		} else {
			return false;
		}	
	}
	
	private boolean isListEmpty(List<String> data) {
		if (data == null || data.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
}
