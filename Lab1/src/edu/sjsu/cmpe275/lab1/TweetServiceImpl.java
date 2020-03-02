package edu.sjsu.cmpe275.lab1;

import java.io.IOException;

public class TweetServiceImpl implements TweetService {
	

	public static int counter = 5;

	@Override
	public void tweet(String user, String message) throws IllegalArgumentException, IOException {
		if(counter-- > 0) {
			throw new IOException();
		}
		if(message == null || message.isEmpty() || message.length()>140 || user == null || user.isEmpty()) {
			throw new IllegalArgumentException();
		}
		
		System.out.printf("User %s tweeted message: %s\n", user, message);
		
	}

	@Override
	public void follow(String follower, String followee)
			throws IllegalArgumentException, UnsupportedOperationException, IOException {
		System.out.printf("User %s followed user %s\n", follower, followee);	
	}

	@Override
	public void block(String user, String follower)
			throws IllegalArgumentException, UnsupportedOperationException, IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unblock(String user, String follower) throws UnsupportedOperationException, IOException {
		// TODO Auto-generated method stub
		
	}

}
