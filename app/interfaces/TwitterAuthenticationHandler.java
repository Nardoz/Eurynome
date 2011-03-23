package interfaces;

public interface TwitterAuthenticationHandler {
	void authenticationSuccess(TwitterUserDTO user);
	void authenticationFail();//maybe it could have a "reason" argument
}
