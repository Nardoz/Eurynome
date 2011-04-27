package socialconnector;

import socialconnector.exceptions.SocialProfileException;

public abstract class SocialAccount<T extends SocialProfile> {
	public String userId;
	public String token;
	public String tokenSecret;

	public abstract T getProfile() throws SocialProfileException;

	public SocialAccount(String userId, String token, String tokenSecret) {
		this.userId = userId;
		this.token = token;
		this.tokenSecret = tokenSecret;
	}
}
