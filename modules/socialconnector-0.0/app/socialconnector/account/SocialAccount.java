package socialconnector.account;

import socialconnector.exceptions.SocialProfileException;
import socialconnector.profile.SocialProfile;

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
