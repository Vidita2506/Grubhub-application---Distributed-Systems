package edu.sjsu.cmpe275.aop.tweet.aspect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
			List<String> currentFollowers = stats.getUserFollowersListMap().get(followee);
			if (!currentFollowers.contains(follower)) {
				currentFollowers.add(follower);
			}
		} else {
			List<String> followers = new ArrayList<String>();
			followers.add(follower);
			stats.getUserFollowersListMap().put(followee, followers);
		}
	}

	@AfterReturning("execution(public void edu.sjsu.cmpe275.aop.tweet.TweetService.block(..))")
	public void afterSuccessFullBlock(JoinPoint joinPoint) {
		String user = (String) joinPoint.getArgs()[0];
		String blockedFollower = (String) joinPoint.getArgs()[1];

		if (stats.getUserBlockedFollowersListMap().containsKey(user)) {
			List<String> blockedFollowersList = stats.getUserBlockedFollowersListMap().get(user);
			if (!blockedFollowersList.contains(blockedFollower)) {
				blockedFollowersList.add(blockedFollower);
			}
		} else {
			stats.getUserBlockedFollowersListMap().put(user, new ArrayList<String>(Arrays.asList(blockedFollower)));
		}

		if (stats.getBlockedUserFolloweesMap().containsKey(blockedFollower)) {
			List<String> followeesList = stats.getBlockedUserFolloweesMap().get(blockedFollower);
			if (!followeesList.contains(user)) {
				followeesList.add(user);
			}
		} else {
			stats.getBlockedUserFolloweesMap().put(blockedFollower, new ArrayList<String>(Arrays.asList(user)));
		}
	}

	@AfterReturning("execution(public void edu.sjsu.cmpe275.aop.tweet.TweetService.unblock(..))")
	public void afterSuccessFullUnblock(JoinPoint joinPoint) {
		String user = (String) joinPoint.getArgs()[0];
		String unBlockedFollower = (String) joinPoint.getArgs()[1];
		if (stats.getUserBlockedFollowersListMap().containsKey(user)) {
			List<String> blockedFollowersList = stats.getUserBlockedFollowersListMap().get(user);
			if (blockedFollowersList.contains(unBlockedFollower)) {
				blockedFollowersList.remove(unBlockedFollower);
			}
		}

		if (stats.getBlockedUserFolloweesMap().containsKey(unBlockedFollower)) {
			List<String> followeesList = stats.getBlockedUserFolloweesMap().get(unBlockedFollower);
			if (followeesList.contains(user)) {
				followeesList.remove(user);
			}
		}
	}

	private boolean isListEmpty(List<String> data) {
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
		List<String> blockedUsers = stats.getUserBlockedFollowersListMap().get(user);
		if (isListEmpty(blockedUsers)) {
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
		List<String> currentUserFollowers = stats.getUserFollowersListMap().get(user);
		if (isListEmpty(currentUserFollowers)) {
			return;
		}
		List<String> getUnblockedFollowers = getUnblockedFollowers(user, currentUserFollowers);
		if (isListEmpty(getUnblockedFollowers)) {
			return;
		}
		List<String> currentMsgFollowers = stats.getMessageFollowersListMap().get(msg);
		if (!isListEmpty(currentMsgFollowers)) {
			for (String userFollower : getUnblockedFollowers) {
				if (!currentMsgFollowers.contains(userFollower)) {
					currentMsgFollowers.add(userFollower);
				}
			}
		} else {
			stats.getMessageFollowersListMap().put(msg, getUnblockedFollowers);
		}
	}

	private void setLengthOfLargestTweet(String msg) {
		int msgLength = msg.length();
		if (msgLength > stats.lengthOfLargestTweet) {
			stats.lengthOfLargestTweet = msgLength;
		}
	}

	private List<String> getUnblockedFollowers(String user, List<String> currentUserFollowers) {
		List<String> unblockedFollowers = new ArrayList<>();
		unblockedFollowers.addAll(currentUserFollowers);
		List<String> blockedUsers = stats.getUserBlockedFollowersListMap().get(user);
		if (!isListEmpty(blockedUsers)) {
			for (String blockedUser : blockedUsers) {
				unblockedFollowers.remove(blockedUser);
			}
		}
		return unblockedFollowers;
	}
}
