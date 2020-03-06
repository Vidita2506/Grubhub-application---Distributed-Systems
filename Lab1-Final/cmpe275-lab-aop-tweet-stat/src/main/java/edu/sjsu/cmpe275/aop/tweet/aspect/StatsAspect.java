package edu.sjsu.cmpe275.aop.tweet.aspect;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import edu.sjsu.cmpe275.aop.tweet.TweetStatsServiceImpl;

@Aspect
@Order(0)

/**
 * Aspect to record stats after successful tweet operations
 */
public class StatsAspect {
	@Autowired
	TweetStatsServiceImpl stats;

	@AfterReturning("execution(public void edu.sjsu.cmpe275.aop.tweet.TweetService.tweet(..))")
	public void afterSuccessFullTweet(JoinPoint joinPoint) {
		String user = (String) joinPoint.getArgs()[0];
		String msg = (String) joinPoint.getArgs()[1];

		// Records length of longest tweet
		setLengthOfLargestTweet(msg);

		// Records the users with whom the message is shared
		setMessageFollowersStats(user, msg);

		// Records the user for which this message is blocked
		setBlockedUserByMissedMsgCount(user);

		// Records the total tweet length for this user.
		setTotalTweetLengthForUser(user, msg);
	}

	
	@AfterReturning("execution(public void edu.sjsu.cmpe275.aop.tweet.TweetService.follow(..))")
	public void afterSuccessFullFollow(JoinPoint joinPoint) {
		String follower = (String) joinPoint.getArgs()[0];
		String followee = (String) joinPoint.getArgs()[1];
		
		/* If the followee already has at least one follower, modify the existing 
		   followers list to add new follower, else create a new list
		*/
		if (stats.getUserFollowersListMap().containsKey(followee)) {
			Set<String> currentFollowers = stats.getUserFollowersListMap().get(followee);
		    currentFollowers.add(follower);
		} else {
			Set<String> followers = new HashSet<String>();
			followers.add(follower);
			stats.getUserFollowersListMap().put(followee, followers);
		}
	}

	@AfterReturning("execution(public void edu.sjsu.cmpe275.aop.tweet.TweetService.block(..))")
	public void afterSuccessFullBlock(JoinPoint joinPoint) {
		String user = (String) joinPoint.getArgs()[0];
		String blockedFollower = (String) joinPoint.getArgs()[1];

		if (stats.getUserBlockedFollowersListMap().containsKey(user)) {
			Set<String> blockedFollowersList = stats.getUserBlockedFollowersListMap().get(user);
			blockedFollowersList.add(blockedFollower);
		} else {
			Set<String> newFollowersSet = new HashSet<String>();
			newFollowersSet.add(blockedFollower);
			stats.getUserBlockedFollowersListMap().put(user, newFollowersSet);
		}

		if (stats.getBlockedUserFolloweesMap().containsKey(blockedFollower)) {
			Set<String> followeesList = stats.getBlockedUserFolloweesMap().get(blockedFollower);
			followeesList.add(user);
		} else {
			Set<String> newUserSet = new HashSet<String>();
			newUserSet.add(user);
			stats.getBlockedUserFolloweesMap().put(blockedFollower, newUserSet);
		}
	}

	@AfterReturning("execution(public void edu.sjsu.cmpe275.aop.tweet.TweetService.unblock(..))")
	public void afterSuccessFullUnblock(JoinPoint joinPoint) {
		String user = (String) joinPoint.getArgs()[0];
		String unBlockedFollower = (String) joinPoint.getArgs()[1];
		if (stats.getUserBlockedFollowersListMap().containsKey(user)) {
			Set<String> blockedFollowersList = stats.getUserBlockedFollowersListMap().get(user);
			blockedFollowersList.remove(unBlockedFollower);
		}

		if (stats.getBlockedUserFolloweesMap().containsKey(unBlockedFollower)) {
			Set<String> followeesList = stats.getBlockedUserFolloweesMap().get(unBlockedFollower);
			followeesList.remove(user);
		}
	}

	private boolean isSetEmpty(Set<String> data) {
		if (data == null || data.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
	
	private void setTotalTweetLengthForUser(String user, String msg) {
		Integer currentTotalLength = stats.getUserTotalTweetsLengthMap().get(user);
		if (currentTotalLength == null) {
			stats.getUserTotalTweetsLengthMap().put(user, msg.length());
		} else {
			stats.getUserTotalTweetsLengthMap().put(user, msg.length() + currentTotalLength);
		}
	}

	private void setBlockedUserByMissedMsgCount(String user) {
		Set<String> blockedUsers = stats.getUserBlockedFollowersListMap().get(user);
		if (isSetEmpty(blockedUsers)) {
			return;
		}
		Map<String, Integer> blockedUserByMissedMsgCount = stats.getBlockedUserByMissedMessageCount();
		for (String blockedUser : blockedUsers) {
			Integer missedMsgCount = blockedUserByMissedMsgCount.get(blockedUser);
			if (missedMsgCount == null) {
				blockedUserByMissedMsgCount.put(blockedUser, 1);
			} else {
				blockedUserByMissedMsgCount.put(blockedUser, missedMsgCount + 1);
			}
		}
	}

	private void setMessageFollowersStats(String user, String msg) {
		Set<String> currentUserFollowers = stats.getUserFollowersListMap().get(user);
		if (isSetEmpty(currentUserFollowers)) {
			return;
		}
		Set<String> unblockedFollowers = getUnblockedFollowers(user, currentUserFollowers);
		if (isSetEmpty(unblockedFollowers)) {
			return;
		}
		Set<String> currentMsgFollowers = stats.getMessageFollowersListMap().get(msg);
		if (!isSetEmpty(currentMsgFollowers)) {
			for (String userFollower : unblockedFollowers) {
				currentMsgFollowers.add(userFollower);
			}
		} else {
			stats.getMessageFollowersListMap().put(msg, unblockedFollowers);
		}
	}

	private void setLengthOfLargestTweet(String msg) {
		int msgLength = msg.length();
		if (msgLength > stats.lengthOfLargestTweet) {
			stats.lengthOfLargestTweet = msgLength;
		}
	}

	private Set<String> getUnblockedFollowers(String user, Set<String> currentUserFollowers) {
		Set<String> unblockedFollowers = new HashSet<String>();
		unblockedFollowers.addAll(currentUserFollowers);
		Set<String> blockedUsers = stats.getUserBlockedFollowersListMap().get(user);
		if (!isSetEmpty(blockedUsers)) {
			for (String blockedUser : blockedUsers) {
				unblockedFollowers.remove(blockedUser);
			}
		}
		return unblockedFollowers;
	}
}
