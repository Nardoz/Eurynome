package services.tuitconnect;

import static play.Play.configuration;
import interfaces.tuitconnect.TwitterAccount;
import play.Play;
import play.exceptions.UnexpectedException;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.http.RequestToken;

public class TuitService {
	private final static String CONSUMER_KEY = Play.configuration.getProperty("tuit.consumerKey");
	private final static String CONSUMER_SECRET = Play.configuration.getProperty("tuit.consumerSecret");

	public static Twitter factory(TwitterAccount account) {
		ConfigurationBuilder conf = new ConfigurationBuilder();
		conf.setOAuthConsumerKey(CONSUMER_KEY)
			.setOAuthConsumerSecret(CONSUMER_SECRET)
			.setOAuthAccessToken(account.token)
			.setOAuthAccessTokenSecret(account.tokenSecret);

		TwitterFactory factory = new TwitterFactory(conf.build());
		Twitter twitter = factory.getInstance();

		return twitter;
	}
	
	public static Twitter factory() {
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);

		return twitter;
	}

	public static RequestToken getRequestToken(String callback) throws Exception {
		Twitter twitter = factory();

		RequestToken requestToken = twitter.getOAuthRequestToken(callback);
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