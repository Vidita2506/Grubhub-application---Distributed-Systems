package edu.sjsu.cmpe275.aop.tweet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TweetStatsServiceImpl implements TweetStatsService {
   
	public int lengthOfLargestTweet = 0;

	// key=>User, value=>followers list
	private Map<String, List<String>> userFollowersListMap = new HashMap<>();
	
	// key=>user, value=>blocked followers list
	private Map<String, List<String>> userBlockedFollowersListMap = new HashMap<>();
	
	// key=>Blocked User, value=> List of users who blocked this user
	private Map<String, List<String>> blockedUserFolloweesMap = new HashMap<>();
	
	// key=>message, value=>followers list
	private Map<String, List<String>> messageFollowersListMap = new HashMap<>();
	
	// key=>user, value=> The total length of all tweets
	private Map<String, Integer> userTotalTweetsLengthMap = new HashMap<String, Integer>();
	
	// key=>blocked user, value=> Missed messages count
	private Map<String, Integer> blockedUserByMissedMessageCount = new HashMap<String, Integer>();
	
	@Override
	public void resetStatsAndSystem() {
		lengthOfLargestTweet = 0;
		userFollowersListMap = new HashMap<>();
		userBlockedFollowersListMap = new HashMap<>();
		blockedUserFolloweesMap = new HashMap<>();
		messageFollowersListMap = new HashMap<>();
		userTotalTweetsLengthMap = new HashMap<>();
		blockedUserByMissedMessageCount = new HashMap<>();
	}
    
	@Override
	public int getLengthOfLongestTweet() {
		return lengthOfLargestTweet;
	}

	@Override
	public String getMostFollowedUser() {
		int maxFollowersCount = 0;
		String mostFollowedUser = null;
		for (Entry<String, List<String>> entry : userFollowersListMap.entrySet()) {
			String followee = entry.getKey();
			int numberOfFollowers = entry.getValue().size();
			if (numberOfFollowers > maxFollowersCount) {
				mostFollowedUser = followee;
				maxFollowersCount = numberOfFollowers;
			} else if (numberOfFollowers == maxFollowersCount) {
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
		for (Entry<String, List<String>> entry : messageFollowersListMap.entrySet()) {
			String message = entry.getKey();
			int numberOfMsgFollowers = entry.getValue().size();
			if (numberOfMsgFollowers > maxFollowedMsgCount) {
				mostPopularMsg = message;
				maxFollowedMsgCount = numberOfMsgFollowers;
			} else if (numberOfMsgFollowers == maxFollowedMsgCount) {
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
			} else if (blockedMsgCount == maxMissedMessagesCount) {
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
		for (Entry<String, List<String>> entry : blockedUserFolloweesMap.entrySet()) {
			String blockedFollower = entry.getKey();
			int numberOfBlockedByFollowees = entry.getValue().size();
			if (numberOfBlockedByFollowees > maxBlockedByFolloweesCount) {
				mostBlockedFollower = blockedFollower;
				maxBlockedByFolloweesCount = numberOfBlockedByFollowees;
			} else if (numberOfBlockedByFollowees == maxBlockedByFolloweesCount) {
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

	public Map<String, List<String>> getUserFollowersListMap() {
		return userFollowersListMap;
	}

	public Map<String, List<String>> getUserBlockedFollowersListMap() {
		return userBlockedFollowersListMap;
	}

	public Map<String, List<String>> getBlockedUserFolloweesMap() {
		return blockedUserFolloweesMap;
	}

	public Map<String, List<String>> getMessageFollowersListMap() {
		return messageFollowersListMap;
	}

	public Map<String, Integer> getUserTotalTweetsLengthMap() {
		return userTotalTweetsLengthMap;
	}

	public Map<String, Integer> getBlockedUserByMissedMessageCount() {
		return blockedUserByMissedMessageCount;
	}
}



