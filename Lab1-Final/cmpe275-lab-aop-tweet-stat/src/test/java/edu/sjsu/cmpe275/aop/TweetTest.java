package edu.sjsu.cmpe275.aop;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.sjsu.cmpe275.aop.tweet.TweetService;
import edu.sjsu.cmpe275.aop.tweet.TweetStatsService;
import org.junit.Assert;

public class TweetTest {

	private TweetService tweeter;

	private TweetStatsService stats;

	private ClassPathXmlApplicationContext ctx;

	@Before
	public void setup() {
		ctx = new ClassPathXmlApplicationContext("context.xml");
		tweeter = (TweetService) ctx.getBean("tweetService");
		stats = (TweetStatsService) ctx.getBean("tweetStatsService");
	}

	@After
	public void end() {
		ctx.close();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTweetUserNull() throws IllegalArgumentException, IOException {
		tweeter.tweet(null, "first tweet");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTweetUserEmpty() throws IllegalArgumentException, IOException {
		tweeter.tweet("", "first tweet");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMsgLengthGreaterThan140() throws IllegalArgumentException, IOException {
		String x = "";
		for (int i = 0; i <= 140; i++) {
			x = x.concat("a");
		}
		tweeter.tweet("User", x);
	}

	@Test()
	public void testTweetSuccesful() throws IllegalArgumentException, IOException {
		tweeter.tweet("user1", "first tweet");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBlockUserNull() throws IllegalArgumentException, IOException {
		tweeter.block(null, "Bob");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBlockUserEmpty() throws IllegalArgumentException, IOException {
		tweeter.block("", "Bob");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBlockFollowerNull() throws IllegalArgumentException, IOException {
		tweeter.block("Alice", null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBlockFollowerEmpty() throws IllegalArgumentException, IOException {
		tweeter.block("Alice", "");
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testBlockUserFollowerAreSame() throws IllegalArgumentException, IOException {
		tweeter.block("Alice", "Alice");
	}

	@Test
	public void testBlockSuccess() throws IllegalArgumentException, IOException {
		tweeter.block("Alice", "Bob");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUnblockUserNull() throws IllegalArgumentException, IOException {
		tweeter.unblock(null, "Bob");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUnblockUserEmpty() throws IllegalArgumentException, IOException {
		tweeter.unblock("", "Bob");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUnblockFollowerNull() throws IllegalArgumentException, IOException {
		tweeter.unblock("Alice", null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUnblockFollowerEmpty() throws IllegalArgumentException, IOException {
		tweeter.unblock("Alice", "");
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testUnblockUserFollowerAreSame() throws IllegalArgumentException, IOException {
		tweeter.unblock("Alice", "Alice");
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testUnblockFollowerNotBlocked() throws IllegalArgumentException, IOException {
		tweeter.unblock("Tom", "Alex");
	}

	@Test
	public void testUnblockSuccess() throws IllegalArgumentException, IOException {
		tweeter.block("Tom", "Alex");
		tweeter.unblock("Tom", "Alex");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFollowFollowerNull() throws IllegalArgumentException, IOException {
		tweeter.follow(null, "Bob");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFollowFollowerEmpty() throws IllegalArgumentException, IOException {
		tweeter.follow("", "Bob");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFollowFolloweeNull() throws IllegalArgumentException, IOException {
		tweeter.follow("Alice", null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFollowFolloweeEmpty() throws IllegalArgumentException, IOException {
		tweeter.follow("Alice", "");
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testFollowFollowerFolloweeAreSame() throws IllegalArgumentException, IOException {
		tweeter.follow("Alice", "Alice");
	}

	@Test()
	public void testFollowSuccess() throws IllegalArgumentException, IOException {
		tweeter.follow("Alice", "Bob");
	}

	@Test()
	public void testGetLengthOfLongestTweet() throws IllegalArgumentException, IOException {
		int length = stats.getLengthOfLongestTweet();
		Assert.assertEquals(length, 0);

		tweeter.tweet("Alice", "Hello World");
		tweeter.tweet("Bob", "1234567890123");

		length = stats.getLengthOfLongestTweet();
		Assert.assertEquals(length, 13);

		String x = "";
		for (int i = 0; i <= 140; i++) {
			x = x.concat("a");
		}
		try {
			tweeter.tweet("User", x);
		} catch (Exception ex) {

		}

		length = stats.getLengthOfLongestTweet();
		Assert.assertEquals(length, 13);

		stats.resetStatsAndSystem();
		tweeter.tweet("foo", "barbar");
		length = stats.getLengthOfLongestTweet();
		Assert.assertEquals(length, 6);
	}

	@Test()
	public void testGetMostFollowedUser() throws IllegalArgumentException, IOException {
		String mostFollowedUser = stats.getMostFollowedUser();
		Assert.assertEquals(mostFollowedUser, null);

		try {
			tweeter.follow("Alice", "Bob");
			tweeter.follow("Charlie", "Bob");
			tweeter.follow("Bob", "Alice");

			mostFollowedUser = stats.getMostFollowedUser();
			Assert.assertEquals(mostFollowedUser, "Bob");

			tweeter.follow("Charlie", "Alice");
			tweeter.block("Alice", "Charlie");
			mostFollowedUser = stats.getMostFollowedUser();
			Assert.assertEquals(mostFollowedUser, "Alice");

			tweeter.follow("Bob", "Bob");

		} catch (Exception ex) {

		}
		tweeter.follow("Charlie", "Bob");
		mostFollowedUser = stats.getMostFollowedUser();
		Assert.assertEquals(mostFollowedUser, "Alice");

		tweeter.follow("James", "Bob");
		mostFollowedUser = stats.getMostFollowedUser();
		Assert.assertEquals(mostFollowedUser, "Bob");

		stats.resetStatsAndSystem();

		try {
			tweeter.follow("Alice", "Bob");
			tweeter.follow("Carl", "Bob");
		} catch (Exception ex) {
			
		}

		tweeter.follow("Bob", "Alice");
		mostFollowedUser = stats.getMostFollowedUser();
		Assert.assertEquals("Alice", mostFollowedUser);
	}

	@Test
	public void testGetMostPopularMessage() throws IllegalArgumentException, IOException {
		String mostPopularMessage = stats.getMostPopularMessage();
		Assert.assertEquals(mostPopularMessage, null);

		try {
			tweeter.tweet("Alice", "Hello");
			mostPopularMessage = stats.getMostPopularMessage();
			Assert.assertEquals(mostPopularMessage, null);

			tweeter.follow("Alice", "Bob");
			tweeter.follow("John", "Charlie");

			tweeter.tweet("Bob", "Ayyee");
			tweeter.tweet("Charlie", "Yayyy");

			mostPopularMessage = stats.getMostPopularMessage();
			Assert.assertEquals(mostPopularMessage, "Ayyee");

			tweeter.tweet("Bob", "Yayyy");
			mostPopularMessage = stats.getMostPopularMessage();
			Assert.assertEquals("Yayyy", mostPopularMessage);

			tweeter.follow("Mike", "Charlie");
			tweeter.tweet("Charlie", "Newwww");
			mostPopularMessage = stats.getMostPopularMessage();
			Assert.assertEquals("Newwww", mostPopularMessage);

			tweeter.block("Charlie", "Mike");
			tweeter.tweet("Charlie", "MM");
			mostPopularMessage = stats.getMostPopularMessage();
			Assert.assertEquals("Newwww", mostPopularMessage);
			
			tweeter.unblock("Charlie", "Mike");
			tweeter.tweet("Charlie", "MM");
			mostPopularMessage = stats.getMostPopularMessage();
			Assert.assertEquals("MM", mostPopularMessage);

			stats.resetStatsAndSystem();
			mostPopularMessage = stats.getMostPopularMessage();
			Assert.assertEquals(mostPopularMessage, null);
		} catch (Exception ex) {

		}
	}

	@Test
	public void testGetMostProductiveUser() throws IllegalArgumentException, IOException {
		String mostProductiveUser = stats.getMostProductiveUser();
		Assert.assertEquals(null, mostProductiveUser);

		tweeter.tweet("Bob", "Ayyee Hello Hello Hello");
		tweeter.tweet("Charlie", "Yayyy");
		tweeter.tweet("Charlie", "Hello");

		mostProductiveUser = stats.getMostProductiveUser();
		Assert.assertEquals("Bob", mostProductiveUser);

		tweeter.tweet("Alice", "Byyee Hello Hello Hello");
		mostProductiveUser = stats.getMostProductiveUser();
		Assert.assertEquals("Alice", mostProductiveUser);

		stats.resetStatsAndSystem();
		mostProductiveUser = stats.getMostProductiveUser();
		Assert.assertEquals(null, mostProductiveUser);
		
		stats.resetStatsAndSystem();
		tweeter.tweet("Alice", "Hello");
		
		mostProductiveUser = stats.getMostProductiveUser();
		Assert.assertEquals("Alice", mostProductiveUser);
	}

	@Test
	public void testMostBlockedFollowerByMissedMsg() throws IllegalArgumentException, IOException {
		String mostBlockedFollower = stats.getMostBlockedFollowerByNumberOfMissedTweets();
		Assert.assertEquals(null, mostBlockedFollower);

		tweeter.block("Alice", "Bob");
		tweeter.block("John", "Bob");
		tweeter.block("Mike", "Charlie");

		tweeter.tweet("Mike", "Hello world");

		mostBlockedFollower = stats.getMostBlockedFollowerByNumberOfMissedTweets();
		Assert.assertEquals("Charlie", mostBlockedFollower);

		tweeter.tweet("Alice", "Hey I am Alicwe");
		mostBlockedFollower = stats.getMostBlockedFollowerByNumberOfMissedTweets();
		Assert.assertEquals("Bob", mostBlockedFollower);

		tweeter.tweet("Mike", "Hey I am messaging again");
		mostBlockedFollower = stats.getMostBlockedFollowerByNumberOfMissedTweets();
		Assert.assertEquals("Charlie", mostBlockedFollower);

		tweeter.tweet("John", "Hey I am John");
		mostBlockedFollower = stats.getMostBlockedFollowerByNumberOfMissedTweets();
		Assert.assertEquals("Bob", mostBlockedFollower);

		tweeter.unblock("Mike", "Charlie");
		tweeter.tweet("Mike", "Hey I am Mike");
		mostBlockedFollower = stats.getMostBlockedFollowerByNumberOfMissedTweets();
		Assert.assertEquals("Bob", mostBlockedFollower);

		stats.resetStatsAndSystem();
		mostBlockedFollower = stats.getMostBlockedFollowerByNumberOfMissedTweets();
		Assert.assertEquals(null, mostBlockedFollower);
	}

	@Test
	public void testMostBlockedFollowerByNumberOfFollowees() throws IllegalArgumentException, IOException {
		String mostBlockedFollower = stats.getMostBlockedFollowerByNumberOfFollowees();
		Assert.assertEquals(null, mostBlockedFollower);

		tweeter.block("Alice", "Bob");
		tweeter.block("John", "Bob");
		tweeter.block("Mike", "Charlie");

		tweeter.tweet("Mike", "Hello world");

		mostBlockedFollower = stats.getMostBlockedFollowerByNumberOfFollowees();
		Assert.assertEquals("Bob", mostBlockedFollower);

		tweeter.tweet("Alice", "Hey I am Alicwe");
		mostBlockedFollower = stats.getMostBlockedFollowerByNumberOfFollowees();
		Assert.assertEquals("Bob", mostBlockedFollower);

		tweeter.block("Amy", "Charlie");
		mostBlockedFollower = stats.getMostBlockedFollowerByNumberOfFollowees();
		Assert.assertEquals("Bob", mostBlockedFollower);

		tweeter.block("Andy", "Charlie");
		mostBlockedFollower = stats.getMostBlockedFollowerByNumberOfFollowees();
		Assert.assertEquals("Charlie", mostBlockedFollower);

		tweeter.unblock("Andy", "Charlie");
		mostBlockedFollower = stats.getMostBlockedFollowerByNumberOfFollowees();
		Assert.assertEquals("Bob", mostBlockedFollower);

		stats.resetStatsAndSystem();
		mostBlockedFollower = stats.getMostBlockedFollowerByNumberOfFollowees();
		Assert.assertEquals(null, mostBlockedFollower);
	} 
	
	@Test
	public void test1() throws IllegalArgumentException, IOException {
		tweeter.block("Alice", "Bob");
		tweeter.follow("Bob", "Alice");
		tweeter.follow("James", "Zhang");
		
		tweeter.tweet("Alice", "Hello");
		tweeter.tweet("Zhang", "Z am Zhang");
		
		String mostPopularMsg = stats.getMostPopularMessage();
		Assert.assertEquals("Z am Zhang", mostPopularMsg);
		
		tweeter.unblock("Alice", "Bob");
		tweeter.tweet("Alice", "Mello");
		
		mostPopularMsg = stats.getMostPopularMessage();
		Assert.assertEquals("Mello", mostPopularMsg);
		
		String mostBlockedUserByMissedMsg = stats.getMostBlockedFollowerByNumberOfMissedTweets();
		Assert.assertEquals("Bob", mostBlockedUserByMissedMsg);
		
		String mostBlockedUserByFollowees = stats.getMostBlockedFollowerByNumberOfFollowees();
		Assert.assertEquals(null, mostBlockedUserByFollowees);
		
		tweeter.block("James", "Zhang");
		mostBlockedUserByFollowees = stats.getMostBlockedFollowerByNumberOfFollowees();
		Assert.assertEquals("Zhang", mostBlockedUserByFollowees);
	}
	
	@Test
	public void test2() throws IllegalArgumentException, IOException {
		tweeter.follow("Alice", "Bob");
		tweeter.follow("Charles", "Alice");
		tweeter.follow("Alice", "Bob");
		
		String mostFollowedUser = stats.getMostFollowedUser();
		Assert.assertEquals("Alice", mostFollowedUser);
	}
	
	@Test
	public void test3() throws IllegalArgumentException, IOException {
		tweeter.follow("Alice", "Bob");
		tweeter.follow("Alice", "Charles");
		tweeter.follow("James", "Robin");
		tweeter.follow("Siri", "Robin");
		
		tweeter.tweet("Bob", "Hello");
		tweeter.tweet("Charles", "Hello");
		tweeter.tweet("Robin", "Zello");
		
		String mostPopularMsg = stats.getMostPopularMessage();
		Assert.assertEquals("Zello", mostPopularMsg);
	}
	
	@Test
	public void test4() throws IllegalArgumentException, IOException {
		tweeter.block("Alice", "Bob");
		tweeter.block("James", "Alice");
		tweeter.block("Alice", "Bob");
		tweeter.block("Alice", "Bob");
		
		String mostBlockedFollowerByFollowees = stats.getMostBlockedFollowerByNumberOfFollowees();
		Assert.assertEquals("Alice", mostBlockedFollowerByFollowees);
	}
}