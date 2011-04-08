package socialconnector;

import socialconnector.account.SocialAccount;
import twitter4j.TwitterException;

public interface SocialPlatformAuthenticationHandler<T extends SocialAccount> {
	void handleAuthenticationSuccess(T account) throws InstantiationException, IllegalAccessException, IllegalStateException, TwitterException;
	void handleAuthenticationFail() throws InstantiationException, IllegalAccessException;
}
