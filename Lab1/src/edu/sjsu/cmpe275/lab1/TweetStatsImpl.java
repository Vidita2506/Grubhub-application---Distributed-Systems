package edu.sjsu.cmpe275.lab1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TweetStatsImpl implements TweetStats {

	public static int lengthOfLargestTweet = 0;

	public static Map<String, List<String>> followersMap = new HashMap<>();

	public static Map<String, Integer> messageFollowerCountMap = new HashMap<String, Integer>();

	public static Map<String, Integer> userTweetLengthMap = new HashMap<String, Integer>();
	
	public static Map<String, List<String>> blockerUsersMap = new HashMap<String, List<String>>();
	
	public static Map<String, List<String>> userBockedMap = new HashMap<String, List<String>>();
	
	public static Map<String, Integer> blockedUserByMessages = new HashMap<String, Integer>();
	
	@Override
	public void resetStatsandSystem() {
		lengthOfLargestTweet = 0;
		followersMap = new HashMap<>();
		messageFollowerCountMap = new HashMap<>();
		userTweetLengthMap = new HashMap<>();
		blockerUsersMap = new HashMap<>();
		blockedUserByMessages = new HashMap<>();
		userBockedMap = new HashMap<>();
	}

	@Override
	public int getLengthOfLongestTweet() {
		return lengthOfLargestTweet;
	}

	@Override
	public String getMostFollowedUser() {
		int maxFollowers = 0;
		String mostFollowedUser = null;
		for (Entry<String, List<String>> entry : followersMap.entrySet()) {
			String followee = entry.getKey();
			int numberOfFollowers = entry.getValue().size();
			if (numberOfFollowers > maxFollowers) {
				mostFollowedUser = followee;
				maxFollowers = numberOfFollowers;
			} else if (numberOfFollowers == maxFollowers) {
				if (followee.compareToIgnoreCase(mostFollowedUser) < 0) {
					mostFollowedUser = followee;
					maxFollowers = numberOfFollowers;
				}
			}
		}
		return mostFollowedUser;
	}

	@Override
	public String getMostPopularMessage() {
		int maxPopularMsgFollowerCount = 0;
		String mostPopularMsg = null;
		for (Entry<String, Integer> entry : messageFollowerCountMap.entrySet()) {

			String msg = entry.getKey();
			int numberOfFollowersForMsg = entry.getValue();

			if (numberOfFollowersForMsg > maxPopularMsgFollowerCount) {
				mostPopularMsg = msg;
				maxPopularMsgFollowerCount = numberOfFollowersForMsg;
			} else if (numberOfFollowersForMsg != 0 && numberOfFollowersForMsg == maxPopularMsgFollowerCount) {
				if (mostPopularMsg == null || msg.compareToIgnoreCase(mostPopularMsg) < 0) {
					mostPopularMsg = msg;
					maxPopularMsgFollowerCount = numberOfFollowersForMsg;
				}
			}
		}
		return mostPopularMsg;
	}

	@Override
	public String getMostProductiveUser() {
		int maxTotalTweetLength = 0;
		String mostProductiveUser = null;
		for (Entry<String, Integer> entry : userTweetLengthMap.entrySet()) {
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
	public String getMostBlockedFollowerByNumberOfMissedMessages() {
		int maxBlockedMessagesCount = 0;
		String mostBlockedUserByMissedMsg = null;
		for (Entry<String, Integer> entry : blockedUserByMessages.entrySet()) {
			String user = entry.getKey();
			int blockedMsgCount = entry.getValue();
			if (blockedMsgCount > maxBlockedMessagesCount) {
				mostBlockedUserByMissedMsg = user;
				maxBlockedMessagesCount = blockedMsgCount;
			} else if (blockedMsgCount == maxBlockedMessagesCount) {
				if (mostBlockedUserByMissedMsg == null || user.compareToIgnoreCase(mostBlockedUserByMissedMsg) < 0) {
					mostBlockedUserByMissedMsg = user;
					maxBlockedMessagesCount = blockedMsgCount;
				}
			}
		}
		return mostBlockedUserByMissedMsg;
	}

	@Override
	public String getMostBlockedFollowerByNumberOfFollowees() {
		int maxBlockedByCount = 0;
		String mostBlockedUser = null;
		for (Entry<String, List<String>> entry : blockerUsersMap.entrySet()) {
			String blockedUser = entry.getKey();
			int blockedByCount = entry.getValue().size();
			if (blockedByCount > maxBlockedByCount) {
				mostBlockedUser = blockedUser;
				maxBlockedByCount = blockedByCount;
			} else if (blockedByCount == maxBlockedByCount) {
				if (mostBlockedUser == null || blockedUser.compareToIgnoreCase(mostBlockedUser) < 0) {
					mostBlockedUser = blockedUser;
					maxBlockedByCount = blockedByCount;
				}
			}
		}
		return mostBlockedUser;
	}

}
