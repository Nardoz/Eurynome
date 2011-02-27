package services;

import play.Play;
import models.Account;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.http.RequestToken;

public class TwitterConnect {
	private final static String CONSUMER_KEY = Play.configuration.getProperty("twitter.consumerKey");
	private final static String CONSUMER_SECRET = Play.configuration.getProperty("twitter.consumerSecret");

	public static Twitter factory(Account user) {
		ConfigurationBuilder conf = new ConfigurationBuilder();
		conf.setOAuthConsumerKey(CONSUMER_KEY).setOAuthConsumerSecret(CONSUMER_SECRET).setOAuthAccessToken(user.token)
			.setOAuthAccessTokenSecret(user.tokenSecret);

		TwitterFactory factory = new TwitterFactory(conf.build());
		Twitter twitter = factory.getInstance();

		return twitter;
	}

	public static Twitter factory() {
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);

		return twitter;
	}

	public static RequestToken getRequestToken(String callback) throws TwitterException {
		Twitter twitter = factory();

		RequestToken requestToken = twitter.getOAuthRequestToken(callback);
		return requestToken;
	}

}
