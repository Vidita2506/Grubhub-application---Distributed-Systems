package edu.sjsu.cmpe275.aop.tweet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class TweetStatsServiceImpl implements TweetStatsService {
   
	public int lengthOfLargestTweet = 0;

	// key=>User, value=>followers for the user
	private Map<String, Set<String>> userFollowersSetMap = new HashMap<String, Set<String>>();
	
	// key=>user, value=>followers that are blocked
	private Map<String, Set<String>> userBlockedFollowersSetMap = new HashMap<String, Set<String>>();
	
	// key=>Blocked User, value=> Set of users who blocked the user
	private Map<String, Set<String>> blockedUserFolloweesMap = new HashMap<String, Set<String>>();
	
	// key=>message, value=>followers for the message
	private Map<String, Set<String>> messageFollowersSetMap = new HashMap<String, Set<String>>();
	
	// key=>user, value=> The total length of all tweets shared by the user
	private Map<String, Integer> userTotalTweetsLengthMap = new HashMap<String, Integer>();
	
	// key=>blocked user, value=> Missed messages count
	private Map<String, Integer> blockedUserByMissedMessageCount = new HashMap<String, Integer>();
	
	@Override
	public void resetStatsAndSystem() {
		lengthOfLargestTweet = 0;
		userFollowersSetMap = new HashMap<String, Set<String>>();
		userBlockedFollowersSetMap = new HashMap<String, Set<String>>();
		blockedUserFolloweesMap = new HashMap<String, Set<String>>();
		messageFollowersSetMap = new HashMap<String, Set<String>>();
		userTotalTweetsLengthMap = new HashMap<String, Integer>();
		blockedUserByMissedMessageCount = new HashMap<String, Integer>();
	}
    
	@Override
	public int getLengthOfLongestTweet() {
		return lengthOfLargestTweet;
	}

	@Override
	public String getMostFollowedUser() {
		int maxFollowersCount = 0;
		String mostFollowedUser = null;
		for (Entry<String, Set<String>> entry : userFollowersSetMap.entrySet()) {
			String followee = entry.getKey();
			int numberOfFollowers = entry.getValue().size();
			if (numberOfFollowers > maxFollowersCount) {
				mostFollowedUser = followee;
				maxFollowersCount = numberOfFollowers;
			} else if (numberOfFollowers != 0 && (numberOfFollowers == maxFollowersCount)) {
				if (mostFollowedUser == null || followee.compareToIgnoreCase(mostFollowedUser) < 0) {
					mostFollowedUser = followee;
					maxFollowersCount = numberOfFollowers;
				}
			}
		}
		return mostFollowedUser;
	}

	@Override
	public String getMostPopularMessage() {
		int maxFollowedMsgCount = 0;
		String mostPopularMsg = null;
		for (Entry<String, Set<String>> entry : messageFollowersSetMap.entrySet()) {
			String message = entry.getKey();
			int numberOfMsgFollowers = entry.getValue().size();
			if (numberOfMsgFollowers > maxFollowedMsgCount) {
				mostPopularMsg = message;
				maxFollowedMsgCount = numberOfMsgFollowers;
			} else if (numberOfMsgFollowers != 0 && (numberOfMsgFollowers == maxFollowedMsgCount)) {
				if (mostPopularMsg == null || message.compareToIgnoreCase(mostPopularMsg) < 0) {
					mostPopularMsg = message;
					maxFollowedMsgCount = numberOfMsgFollowers;
				}
			}
		}
		return mostPopularMsg;
	}
	
	@Override
	public String getMostProductiveUser() {
		int maxTotalTweetLength = 0;
		String mostProductiveUser = null;
		for (Entry<String, Integer> entry : userTotalTweetsLengthMap.entrySet()) {
			String user = entry.getKey();
			int tweetLength = entry.getValue();
			if (tweetLength > maxTotalTweetLength) {
				mostProductiveUser = user;
				maxTotalTweetLength = tweetLength;
			} else if (tweetLength == maxTotalTweetLength) {
				if (mostProductiveUser == null || user.compareToIgnoreCase(mostProductiveUser) < 0) {
					mostProductiveUser = user;
					maxTotalTweetLength = tweetLength;
				}
			}
		}
		return mostProductiveUser;
	}

	@Override
	public String getMostBlockedFollowerByNumberOfMissedTweets() {
		int maxMissedMessagesCount = 0;
		String mostBlockedUserByMissedMsg = null;
		for (Entry<String, Integer> entry : blockedUserByMissedMessageCount.entrySet()) {
			String blockedUser = entry.getKey();
			int blockedMsgCount = entry.getValue();
			if (blockedMsgCount > maxMissedMessagesCount) {
				mostBlockedUserByMissedMsg = blockedUser;
				maxMissedMessagesCount = blockedMsgCount;
			} else if (blockedMsgCount !=0 && (blockedMsgCount == maxMissedMessagesCount)) {
				if (mostBlockedUserByMissedMsg == null || blockedUser.compareToIgnoreCase(mostBlockedUserByMissedMsg) < 0) {
					mostBlockedUserByMissedMsg = blockedUser;
					maxMissedMessagesCount = blockedMsgCount;
				}
			}
		}
		return mostBlockedUserByMissedMsg;
	}

	@Override
	public String getMostBlockedFollowerByNumberOfFollowees() {
		int maxBlockedByFolloweesCount = 0;
		String mostBlockedFollower = null;
		for (Entry<String, Set<String>> entry : blockedUserFolloweesMap.entrySet()) {
			String blockedFollower = entry.getKey();
			
			Set<String> followees = entry.getValue();
			Set<String> actualFollowees = new HashSet<String>();
			for (String followee : followees) {
				if (userFollowersSetMap.containsKey(followee)) {
					if (userFollowersSetMap.get(followee).contains(blockedFollower)) {
						actualFollowees.add(followee);
					}
				}
			}
			
			int numberOfBlockedByFollowees = actualFollowees.size();
			if (numberOfBlockedByFollowees > maxBlockedByFolloweesCount) {
				mostBlockedFollower = blockedFollower;
				maxBlockedByFolloweesCount = numberOfBlockedByFollowees;
			} else if (numberOfBlockedByFollowees != 0 && (numberOfBlockedByFollowees == maxBlockedByFolloweesCount)) {
				if (mostBlockedFollower == null || blockedFollower.compareToIgnoreCase(mostBlockedFollower) < 0) {
					mostBlockedFollower = blockedFollower;
					maxBlockedByFolloweesCount = numberOfBlockedByFollowees;
				}
			}
		}
		return mostBlockedFollower;
	}

	public int getLengthOfLargestTweet() {
		return lengthOfLargestTweet;
	}

	public Map<String, Set<String>> getUserFollowersListMap() {
		return userFollowersSetMap;
	}

	public Map<String, Set<String>> getUserBlockedFollowersListMap() {
		return userBlockedFollowersSetMap;
	}

	public Map<String, Set<String>> getBlockedUserFolloweesMap() {
		return blockedUserFolloweesMap;
	}

	public Map<String, Set<String>> getMessageFollowersListMap() {
		return messageFollowersSetMap;
	}

	public Map<String, Integer> getUserTotalTweetsLengthMap() {
		return userTotalTweetsLengthMap;
	}

	public Map<String, Integer> getBlockedUserByMissedMessageCount() {
		return blockedUserByMissedMessageCount;
	}
}



