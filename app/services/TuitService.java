package services;

import play.Play;
import play.exceptions.UnexpectedException;
import twitter4j.*;
import twitter4j.http.*;
import static play.Play.configuration;

public class TuitService {

	private final static String CONSUMER_KEY = Play.configuration.getProperty("tuit.consumerKey");
	private final static String CONSUMER_SECRET = Play.configuration.getProperty("tuit.consumerSecret");

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

	public static void checkConfiguration() {
		String value;

		if ( (value=configuration.getProperty("tuit.consumerKey")) == null || "".equals(value)) {
			throw new UnexpectedException("No 'tuit.consumerKey' defined in application.conf");
		}
		if (value.matches("x*")) {
			throw new UnexpectedException("You have to initialize 'tuit.consumerKey' in application.conf");
		}

		if ( (value=configuration.getProperty("tuit.consumerSecret")) == null || "".equals(value)) {
			throw new UnexpectedException("No 'tuit.consumerSecret' defined in application.conf");
		}
		if (value.matches("x*")) {
			throw new UnexpectedException("You have to initialize 'tuit.consumerSecret' in application.conf");
		}

		/* TODO: not implemented yet 
		if ( (value=configuration.getProperty("tuit.onSignedIn")) == null || "".equals(value)) {
			throw new UnexpectedException("No 'tuit.onSignedIn' defined in application.conf");
		}
		Router.getFullUrl(value); // to check if the action is routed
		
		if ( (value=configuration.getProperty("tuit.onSignedOut")) == null || "".equals(value)) {
			throw new UnexpectedException("No 'tuit.onSignedOut' defined in application.conf");
		}
		Router.getFullUrl(value); // to check if the action is routed
		*/
	}

}