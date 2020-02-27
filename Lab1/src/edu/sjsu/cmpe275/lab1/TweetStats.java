package edu.sjsu.cmpe275.lab1;

public interface TweetStats {
	/**
	 * reset all the measurements and all the following/blocking relationships as
	 * well.
	 */
	void resetStatsandSystem();

	/**
	 * @return the length of the longest message successfully sent since the
	 *         beginning or last reset. Cannot be more than 140. If no messages
	 *         succeeded, return 0. * Failed messages are not counted for this.
	 */
	int getLengthOfLongestTweet();

	/**
	 * @return the user who has been followed by the biggest number of different
	 *         users since the beginning or last reset. If there is a tie, return
	 *         the 1st of such users based on the alphabetical order. If the follow
	 *         action did not succeed (e.g., due to network errors), it does not
	 *         count toward the stats. If no users were successfully followed,
	 *         return null. Blocking or not does not affect this metric.
	 */
	String getMostFollowedUser();

	/**
	 * @return the message that has been shared with the biggest number of different
	 *         followers when it is successfully tweeted. If the same message (based
	 *         on string equality, case sensitive) has been tweeted more than once,
	 *         it is considered as the same message for this purpose. Return based
	 *         on dictionary order if there is a tie.
	 */
	String getMostPopularMessage();

	/**
	 * The most productive user is determined by the total length of all the
	 * messages successfully tweeted since the beginning or last reset. If there is
	 * a tie, return the 1st of such users based on alphabetical order. If no users
	 * successfully tweeted, return null. * @return the most productive user.
	 */
	String getMostProductiveUser();

	/**
	 * @return the user who has been successfully blocked for the biggest number of
	 *         different messages since the beginning or last reset. If there is a
	 *         tie, return the 1st of such users based on alphabetical order.
	 * 
	 *         If no follower has been successfully blocked by anyone, return null.
	 */
	String getMostBlockedFollowerByNumberOfMissedMessages();

	/**
	 * @return the user who is currently successfully blocked by the biggest number
	 *         of different users since the beginning or last reset. If there is a
	 *         tie, return the 1st of such users based on alphabetical order. If no
	 *         follower has been successfully blocked by anyone, return null.
	 */
	String getMostBlockedFollowerByNumberOfFollowees();
}
