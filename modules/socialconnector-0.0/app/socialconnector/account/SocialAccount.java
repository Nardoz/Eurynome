package socialconnector.account;

import java.util.Map;
import play.db.jpa.GenericModel;
import socialconnector.profile.SocialProfile;
import twitter4j.TwitterException;

public abstract class SocialAccount<T extends SocialProfile> {
	public String userId;
	public String token;
	public String tokenSecret;
	public abstract T getProfile() throws IllegalStateException, TwitterException;
	
	public SocialAccount(String userId, String token, String tokenSecret) {
		this.userId = userId;
		this.token = token;
		this.tokenSecret = tokenSecret;
	}
}
