package edu.sjsu.cmpe275.aop.tweet;

import java.io.IOException;

public interface TweetService {
	
	// Please do NOT change this file.

	/**
	 * @throws IllegalArgumentException if the message is more than 140 characters
	 *                                  as measured by string length, or any
	 *                                  parameter is null or empty.
	 * @throws IOException              if there is a network failure.
	 */
	void tweet(String user, String message) throws IllegalArgumentException, IOException;

	/**
	 * If Alice follows Bob, and Bob is not currently blocking Alice, any future
	 * message that Bob tweets after this are shared with Alice. If at any point Bob
	 * blocks Alice, the sharing after blocking will be stopped.
	 * 
	 * @throws IllegalArgumentException      if either parameter is null or empty
	 * @throws UnsupportedOperationException if follower and followee are two
	 *                                       non-empty strings and equal to each
	 *                                       other.
	 * 
	 * @throws IOException                   if there is a network failure.
	 */

	void follow(String follower, String followee)
			throws IllegalArgumentException, UnsupportedOperationException, IOException;

	/**
	 * This method allows a user to block a follower or a potential follower so that
	 * subsequently tweets will not be shared with the latter, unless unblocked. The
	 * same block operation can be repeated.
	 * 
	 * @throws IllegalArgumentException      if the user or follower is null or
	 *                                       empty.
	 * @throws UnsupportedOperationException if attempts to block himself.
	 * @throws IOException                   if there is a network failure
	 */
	void block(String user, String followee) throws IOException, UnsupportedOperationException, IOException;

	/**
	 * This method undoes previous blocks.
	 * 
	 * @throws IOException                   if there is a network failure
	 * @throws IllegalArgumentException      if the follower or followee is null or
	 *                                       empty.
	 * @throws UnsupportedOperationException if follower is not currently blocked by
	 *                                       user, or user attempts to unblock
	 *                                       himself.
	 */
	void unblock(String user, String follower)
			throws IllegalArgumentException, UnsupportedOperationException, IOException;

}
