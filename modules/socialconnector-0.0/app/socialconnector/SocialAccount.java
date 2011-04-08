package socialconnector;

import java.util.Map;
import play.db.jpa.GenericModel;

public abstract class SocialAccount extends GenericModel {
	protected Map<String, Object> data;
	public String token;
	public String tokenSecret;
	
	public Object get(String fieldName) {
		return this.data.get(fieldName);
	}
	
	public SocialAccount(Map<String, Object> data, String token, String tokenSecret) {
		this.data = data;
		this.token = token;
		this.tokenSecret = tokenSecret;
	}
}
