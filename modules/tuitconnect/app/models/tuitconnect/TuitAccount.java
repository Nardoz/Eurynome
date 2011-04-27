package models.tuitconnect;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class TuitAccount extends Model {
	public String token;
	public String tokenSecret;
	public Long twitterId;
	public String screenName;
	public String locale;
}
