package edu.sjsu.cmpe275.lab1;

import org.springframework.context.support.ClassPathXmlApplicationContext;


public class App {
    public static void main(String[] args) {
    	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("context.xml");
        TweetService tweeter = (TweetService) ctx.getBean("tweetService");
        TweetStats stats = (TweetStats) ctx.getBean("tweetStats");

        try {
        	tweeter.follow("Charlie", "Alice");
        	tweeter.follow("Bob", "Alice");
        	tweeter.follow("Bob", "Charlie");
        	tweeter.follow("Alice", "Charlie");
        	
        	//tweeter.tweet("Charlie", "Hello");
        	//tweeter.tweet("Charlie", "AMy second tweet");
        	tweeter.tweet("WAlice", "Hello");
        	tweeter.tweet("Bob", "Hello");
        	     	
        	System.out.println(stats.getLengthOfLongestTweet());
        	System.out.println(stats.getMostFollowedUser());
        	System.out.println(stats.getMostPopularMessage());
        	System.out.println(stats.getMostProductiveUser());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}