package socialconnector.account;

import java.io.Serializable;
import java.util.Map;
import play.db.jpa.GenericModel;
import socialconnector.exceptions.SocialAccountProfileException;
import socialconnector.profile.SocialProfile;
import twitter4j.TwitterException;

public abstract class SocialAccount<T extends SocialProfile> implements Serializable {

	public static final long serialVersionUID = 20110407L;

	public String userId;
	public String token;
	public String tokenSecret;

	public abstract T getProfile() throws SocialAccountProfileException;

	public SocialAccount(String userId, String token, String tokenSecret) {
		this.userId = userId;
		this.token = token;
		this.tokenSecret = tokenSecret;
	}
}
