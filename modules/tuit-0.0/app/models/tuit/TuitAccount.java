package models.tuit;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.UniqueConstraint;

import play.data.validation.Required;
import play.db.jpa.GenericModel;

@Entity
public class TuitAccount extends GenericModel {

	public TuitAccount(Long userId, String screenName, String token, String tokenSecret) {
		super();

		this.userId = userId;
		this.screenName = screenName;
		this.token = token;
		this.tokenSecret = tokenSecret;
	}

	@Id
	public Long userId;

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
