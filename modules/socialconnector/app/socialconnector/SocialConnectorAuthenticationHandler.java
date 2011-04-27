package socialconnector;

import twitter4j.TwitterException;

public interface SocialConnectorAuthenticationHandler<T extends SocialAccount> {
	void authenticationSuccess(T account) throws IllegalStateException, TwitterException;
	void authenticationFail(); //maybe it could have a "reason" argument
}
