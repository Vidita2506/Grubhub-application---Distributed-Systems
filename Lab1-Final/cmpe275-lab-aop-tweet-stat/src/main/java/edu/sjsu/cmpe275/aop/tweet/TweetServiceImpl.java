package edu.sjsu.cmpe275.aop.tweet;

import java.io.IOException;

public class TweetServiceImpl implements TweetService {
	
	public static int counter = 3;

    /***
     * Following is a dummy implementation.
     * You can tweak the implementation to suit your need, but this file is NOT part of the submission.
     */

	@Override
    public void tweet(String user, String message) throws IllegalArgumentException, IOException {
		if(counter > 0) {
			counter --;
			throw new IOException();
		}
		System.out.printf("User %s tweeted message: %s\n", user, message);
    }

	@Override
    public void follow(String follower, String followee) throws IOException {
		if(counter > 0) {
			counter --;
			throw new IOException();
		}
       	System.out.printf("User %s followed user %s \n", follower, followee);
    }

	@Override
	public void block(String user, String follower) throws IOException {
		if(counter > 0) {
			counter --;
			throw new IOException();
		}
       	System.out.printf("User %s blocked user %s \n", user, follower);		
	}

	@Override
	public void unblock(String user, String follower)
			throws IllegalArgumentException, UnsupportedOperationException, IOException {
		if(counter > 0) {
			counter --;
			throw new IOException();
		}
		System.out.printf("User %s unblocked user %s \n", user, follower);		
	}
}
