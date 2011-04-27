package socialconnector;

public interface SocialPlatformConnector {
	void handleAuthentication(SocialPlatformAuthenticationHandler callback) throws Exception;
}
