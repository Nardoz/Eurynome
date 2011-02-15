package models;

import javax.persistence.Entity;
import javax.persistence.UniqueConstraint;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class User extends Model {

	public User(String screenName, String token, String tokenSecret) {
		super();

		this.screenName = screenName;
		this.token = token;
		this.tokenSecret = tokenSecret;
	}

	@Required
	public String screenName;

	@Required
	public String token;

	@Required
	public String tokenSecret;

	@Override
	public String toString() {
		return this.screenName;
	}
}
