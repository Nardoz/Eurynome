package socialconnector;

public interface SocialPlatformAuthenticationHandler<T extends SocialAccount> {
	void handleAuthenticationSuccess(T account) throws InstantiationException, IllegalAccessException;
	void handleAuthenticationFail() throws InstantiationException, IllegalAccessException;
}
