package models;

import java.net.URL;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class UserDB extends Model {
	public UserDB() {
		super();

		this.follows = new HashSet<UserDB>();
	}

//	@Id
//	public long id;
	public long userId;

	public long createdAt;
	public String description;
	public int favouritesCount;
	public int followersCount;
	public int friendsCount;
	// public int id;
	public String lang;
	public int listedCount;
	public String location;
	public String name;
	public String profileBackgroundColor;
	public String profileBackgroundImageUrl;
	public String profileImageURL;
	public String profileLinkColor;
	public String profileSidebarBorderColor;
	public String profileSidebarFillColor;
	public String profileTextColor;
	public String screenName;
	// public Status Status;
	// public long statusCreatedAt;
	// public long statusId;
	// public String statusInReplyToScreenName;
	// public long statusInReplyToStatusId;
	// public int statusInReplyToUserId;
	// public String statusSource;
	// public String statusText;
	public int statusesCount;
	public String timeZone;
	public String URL;
	public int utcOffset;
	// public RateLimitStatus rateLimitStatus;

	@OneToMany(cascade = CascadeType.ALL)
	public Set<UserDB> follows;

	@Override
	public String toString() {
		return "@" + this.screenName + " (" + this.name + ")";
	}
}
