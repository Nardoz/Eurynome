package services;

import models.UserDB;
import twitter4j.User;

public class UserService {
	// private User user;
	//
	// public UserService(User user) {
	// this.user = user;
	// }

	public static UserDB setFollowing(UserDB dao, User user) {
		UserDB f = findOrCreate(user);

		dao.follows.add(f);
		dao.save();

		return f;
	}

	public static UserDB find(String screenName) {
		UserDB dao = UserDB.find("screenName = ?", screenName).first();
		return dao;

		// throw new UserServiceException("User not found");
	}

	public static UserDB find(Long id) {
		UserDB dao = UserDB.find("userId = ?", id).first();
		return dao;
	}

	public static UserDB findOrCreate(User user) {
		UserDB dao = UserDB.find("userId = ?", new Long(user.getId())).first();

		if (null == dao) {
			dao = new UserDB();

			// UserId from Twitter
			dao.userId = user.getId();

			dao.createdAt = user.getCreatedAt().getTime();
			dao.description = user.getDescription();
			dao.favouritesCount = user.getFavouritesCount();
			dao.followersCount = user.getFollowersCount();
			dao.friendsCount = user.getFriendsCount();
			dao.lang = user.getLang();
			dao.listedCount = user.getListedCount();
			dao.location = user.getLocation();
			dao.name = user.getName();
			dao.profileBackgroundColor = user.getProfileBackgroundColor();
			dao.profileBackgroundImageUrl = user.getProfileBackgroundImageUrl();
			dao.profileImageURL = user.getProfileImageURL().toString();
			dao.profileLinkColor = user.getProfileLinkColor();
			dao.profileSidebarBorderColor = user.getProfileSidebarBorderColor();
			dao.profileSidebarFillColor = user.getProfileSidebarFillColor();
			dao.profileTextColor = user.getProfileTextColor();
			dao.screenName = user.getScreenName();
			// dao.Status = user.getStatus();
			// dao.statusCreatedAt = user.getStatusCreatedAt().getTime();
			// dao.statusId = user.getStatusId();
			// dao.statusInReplyToScreenName =
			// user.getStatusInReplyToScreenName();
			// dao.statusInReplyToStatusId = user.getStatusInReplyToStatusId();
			// dao.statusInReplyToUserId = user.getStatusInReplyToUserId();
			// dao.statusSource = user.getStatusSource();
			// dao.statusText = user.getStatusText();
			dao.statusesCount = user.getStatusesCount();
			dao.timeZone = user.getTimeZone();
			dao.URL = (user.getURL() != null) ? user.getURL().toString() : null;
			dao.utcOffset = user.getUtcOffset();
			// dao.rateLimitStatus = user.getRateLimitStatus();

			dao.save();
		}

		return dao;
	}

}
