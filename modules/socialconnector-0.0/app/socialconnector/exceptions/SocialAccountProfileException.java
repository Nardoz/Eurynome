package socialconnector.exceptions;

import twitter4j.TwitterException;

public class SocialAccountProfileException extends Exception {

	public SocialAccountProfileException(TwitterException e) {
		super(e);
	}
}
