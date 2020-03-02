package edu.sjsu.cmpe275.aop.tweet;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        /***
         * Following is a dummy implementation of App to demonstrate bean creation with Application context.
         * You may make changes to suit your need, but this file is NOT part of the submission.
         */

    	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("context.xml");
        TweetService tweeter = (TweetService) ctx.getBean("tweetService");
        TweetStatsService stats = (TweetStatsService) ctx.getBean("tweetStatsService");

        try {
           // tweeter.follow("alex", "bob");
           // tweeter.follow("bob", "charlie");
           // tweeter.follow("charlie", "bob");
            tweeter.tweet("charlie", "first tweet");
           // tweeter.tweet("bob", "first tweet");
           // tweeter.tweet("bob", "asecond tweet");
         //   tweeter.block("alex", "bob");
           // tweeter.tweet("alex", "second tweet");

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Most productive user: " + stats.getMostProductiveUser());
        System.out.println("Most popular user: " + stats.getMostFollowedUser());
        System.out.println("Length of the longest tweet: " + stats.getLengthOfLongestTweet());
        System.out.println("Most popular message: " + stats.getMostPopularMessage());
        System.out.println("Most blocked follower by followees: " + stats.getMostBlockedFollowerByNumberOfFollowees());
        System.out.println("Most blocked follower by missed tweets: " + stats.getMostBlockedFollowerByNumberOfMissedTweets());
        ctx.close();
    }
}
