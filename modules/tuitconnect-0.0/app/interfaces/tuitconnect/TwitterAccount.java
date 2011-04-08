package interfaces.tuitconnect;

import services.tuitconnect.TuitService;
import socialconnector.account.SocialAccount;
import socialconnector.exceptions.SocialAccountProfileException;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.http.AccessToken;

public class TwitterAccount extends SocialAccount<TwitterUserProfile> {
	public TwitterAccount(String userId, String token, String tokenSecret) {
		super(userId, token, tokenSecret);
	}

	@Override
	public TwitterUserProfile getProfile() throws SocialAccountProfileException {
		Twitter twitter = TuitService.factory(this);
		TwitterUserProfile profile = null;
		User user;
		try {
			user = twitter.verifyCredentials();
			profile = new TwitterUserProfile();
			profile.url = user.getURL();
			profile.screenName = user.getScreenName();
			String[] name = user.getName().split("\\s");
			profile.firstName = name.length > 0 ? name[0] : null;
			profile.lastName = name.length > 1 ? name[1] : null;
			profile.description = user.getDescription();
			profile.followers = user.getFollowersCount();
			profile.following = user.getFriendsCount();
			profile.connectionsCount = profile.followers + profile.following;
			profile.pictureUrl = user.getProfileImageURL();
			profile.location = user.getLocation();
			profile.language = user.getLang();

		} catch (TwitterException e) {
			throw new SocialAccountProfileException(e);
		}

		return profile;
	}
}
