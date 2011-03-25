package socialconnector;

public interface SocialConnectorAuthenticationHandler<T extends SocialAccount> {
	void authenticationSuccess(T account);
	void authenticationFail(); //maybe it could have a "reason" argument
}
