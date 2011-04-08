package interfaces.tuitconnect;

import java.util.HashMap;
import java.util.Map;

import socialconnector.SocialAccount;

public class TwitterUser extends SocialAccount {
	public Long userId;
	public String screenName;
	
	public TwitterUser(String token, String tokenSecret, Long userId, String screenName) {
		super(new HashMap<String,Object>(), token, tokenSecret);
		this.data.put("userId", userId);
		this.data.put("screenName", screenName);
		this.userId = userId;
		this.screenName = screenName;
	}
}
