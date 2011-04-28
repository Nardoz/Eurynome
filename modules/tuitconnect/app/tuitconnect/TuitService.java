package tuitconnect;

import static play.Play.configuration;
import play.Play;
import play.exceptions.UnexpectedException;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class TuitService {
	private final static String CONSUMER_KEY = Play.configuration.getProperty("tuit.consumerKey");
	private final static String CONSUMER_SECRET = Play.configuration.getProperty("tuit.consumerSecret");

	private static Configuration getConfiguration(String token, String tokenSecret) {
		ConfigurationBuilder conf = new ConfigurationBuilder();
		conf.setOAuthConsumerKey(CONSUMER_KEY)
			.setOAuthConsumerSecret(CONSUMER_SECRET);
		
		if(token != null && tokenSecret != null) {
			conf.setOAuthAccessToken(token)
				.setOAuthAccessTokenSecret(tokenSecret);
		}
		
		return conf.build();
	}
	
	private static Configuration getConfiguration(TwitterAccount account) {
		String token = account != null ? account.token : null;
		String tokenSecret = account != null ? account.tokenSecret : null;
		return getConfiguration(token, tokenSecret);
	}
	
	public static Twitter twitterFactory(TwitterAccount account) {
		TwitterFactory factory = new TwitterFactory(getConfiguration(account));
		return factory.getInstance();
	}
	
	public static Twitter twitterFactory() {
		return twitterFactory(null);
	}

	public static TwitterStream streamFactory(String token, String tokenSecret) {
		TwitterStreamFactory factory = new TwitterStreamFactory(getConfiguration(token, tokenSecret));
		return factory.getInstance();
	}
	
	public static TwitterStream streamFactory(TwitterAccount account) {
		TwitterStreamFactory factory = new TwitterStreamFactory(getConfiguration(account));
		return factory.getInstance();
	}

	public static RequestToken getRequestToken(String callback) throws Exception {
		RequestToken requestToken = twitterFactory().getOAuthRequestToken(callback);
		return requestToken;
	}

	public static void checkConfiguration() {
		String value;

		if ( (value=configuration.getProperty("tuit.consumerKey")) == null || "".equals(value)) {
			throw new UnexpectedException("No 'tuit.consumerKey' defined in application.conf");
		}

		if ( (value=configuration.getProperty("tuit.consumerSecret")) == null || "".equals(value)) {
			throw new UnexpectedException("No 'tuit.consumerSecret' defined in application.conf");
		}
	}
}