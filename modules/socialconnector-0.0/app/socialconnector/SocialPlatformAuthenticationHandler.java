package socialconnector;

import twitter4j.TwitterException;

public interface SocialPlatformAuthenticationHandler<T extends SocialAccount> {
	void authenticationSuccess(T account) throws InstantiationException, IllegalAccessException, IllegalStateException, TwitterException;
	void authenticationFail() throws InstantiationException, IllegalAccessException;
}
