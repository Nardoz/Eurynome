package interfaces.tuitconnect;

import java.io.Serializable;

import services.tuitconnect.TuitService;
import socialconnector.account.SocialAccount;
import socialconnector.exceptions.SocialProfileException;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

public class TwitterAccount extends SocialAccount<TwitterUserProfile> implements Serializable {
	public TwitterAccount(String userId, String token, String tokenSecret) {
		super(userId, token, tokenSecret);
	}

	@Override
	public TwitterUserProfile getProfile() throws SocialProfileException {
		Twitter twitter = TuitService.factory(this);
		TwitterUserProfile profile = null;
		try {
			User user = twitter.verifyCredentials();
			profile = new TwitterUserProfile();
			profile.url = user.getURL();
			profile.nickname = user.getScreenName();
			String[] names = user.getName().split("\\s", 2);
			profile.firstName = names[0];
			profile.lastName = names.length > 1 ? names[1] : null;
			profile.followers = user.getFollowersCount();
			profile.following = user.getFriendsCount();
			profile.connectionsCount = profile.followers + profile.following;
			profile.photoUrl = user.getProfileImageURL();
			profile.location = user.getLocation();
			profile.locale = user.getLang();

		} catch (TwitterException e) {
			throw new SocialProfileException(e);
		}

		return profile;
	}
}
