package edu.sjsu.cmpe275.lab1.aspect;

import static edu.sjsu.cmpe275.lab1.TweetStatsImpl.blockedUserByMessages;
import static edu.sjsu.cmpe275.lab1.TweetStatsImpl.blockerUsersMap;
import static edu.sjsu.cmpe275.lab1.TweetStatsImpl.followersMap;
import static edu.sjsu.cmpe275.lab1.TweetStatsImpl.messageFollowerCountMap;
import static edu.sjsu.cmpe275.lab1.TweetStatsImpl.userBockedMap;
import static edu.sjsu.cmpe275.lab1.TweetStatsImpl.userTweetLengthMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

import edu.sjsu.cmpe275.lab1.TweetStatsImpl;

@Aspect
public class StatsAspect {

	@Autowired
	private TweetStatsImpl stats;

	@AfterReturning("execution(public void edu.sjsu.cmpe275.lab1.TweetService.tweet(..))")
	public void afterSuccessfullTweet(JoinPoint joinPoint) {
		String user = (String) joinPoint.getArgs()[0];
		String msg = (String) joinPoint.getArgs()[1];
		if (msg != null) {
			int msgLength = msg.length();
			if (msgLength > TweetStatsImpl.lengthOfLargestTweet) {
				TweetStatsImpl.lengthOfLargestTweet = msgLength;
			}
		}
		if ((user != null && msg != null)) {
			List<String> currentFollowers = followersMap.get(user);

			Integer existingCount = messageFollowerCountMap.get(msg);

			if (existingCount != null) {
				if (currentFollowers != null) {
					messageFollowerCountMap.put(msg, existingCount + currentFollowers.size());
				} else {
					messageFollowerCountMap.put(msg, existingCount);
				}
			} else {
				if (currentFollowers != null) {
					messageFollowerCountMap.put(msg, currentFollowers.size());
				} else {
					messageFollowerCountMap.put(msg, 0);
				}
			}

			// For Most productive user
			Integer currentLength = userTweetLengthMap.get(user);
			if (currentLength == null) {
				userTweetLengthMap.put(user, msg.length());
			} else {
				userTweetLengthMap.put(user, msg.length() + currentLength);
			}
			
			// For most blocked by missed messages
			List<String> usersBlockedList = userBockedMap.get(user);
			if (usersBlockedList != null) {
				for (String blockedUser : usersBlockedList) {
					if (blockedUserByMessages.containsKey(blockedUser)) {
						blockedUserByMessages.put(blockedUser, blockedUserByMessages.get(blockedUser) + 1);
					} else {
						blockedUserByMessages.put(blockedUser, 1);
					}
				}
			}
		}
	}

	@AfterReturning("execution(public void edu.sjsu.cmpe275.lab1.TweetService.follow(..))")
	public void followStats(JoinPoint joinPoint) {
		String follower = (String) joinPoint.getArgs()[0];
		String followee = (String) joinPoint.getArgs()[1];

		if (((follower != null && !follower.isEmpty()) && (followee != null && !followee.isEmpty()))) {
			if (followersMap.containsKey(followee)) {
				List<String> currentFollowers = followersMap.get(followee);
				if (!currentFollowers.contains(follower)) {
					currentFollowers.add(follower);
				}
			} else {
				List<String> followers = new ArrayList<String>();
				followers.add(follower);
				followersMap.put(followee, followers);
			}
		}
	}

	@AfterReturning("execution(public void edu.sjsu.cmpe275.lab1.TweetService.block(..))")
	public void blockStats(JoinPoint joinPoint) {
		String user = (String) joinPoint.getArgs()[0];
		String blockedUser = (String) joinPoint.getArgs()[1];

		if (blockerUsersMap.containsKey(blockedUser)) {
			List<String> blockersList = blockerUsersMap.get(blockedUser);
			if (!blockersList.contains(user)) {
				blockersList.add(user);
			}
		} else {
			blockerUsersMap.put(blockedUser, new ArrayList<String>(Arrays.asList(user)));
		}

		if (userBockedMap.containsKey(user)) {
			List<String> blockedList = userBockedMap.get(user);
			if (!blockedList.contains(blockedUser)) {
				blockedList.add(blockedUser);
			}
		} else {
			userBockedMap.put(user, new ArrayList<String>(Arrays.asList(blockedUser)));
		}

	}
}
