package interfaces.tuitconnect;

import services.tuitconnect.TuitService;
import socialconnector.account.SocialAccount;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.http.AccessToken;

public class TwitterAccount extends SocialAccount<TwitterUserProfile> {
	public TwitterAccount(String userId, String token, String tokenSecret) {
		super(userId, token, tokenSecret);
	}

	@Override
	public TwitterUserProfile getProfile() throws IllegalStateException, TwitterException {
		Twitter twitter = TuitService.factory(this);
		User user = twitter.verifyCredentials();
		
		TwitterUserProfile profile = new TwitterUserProfile();
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
		return profile;
	}
}
