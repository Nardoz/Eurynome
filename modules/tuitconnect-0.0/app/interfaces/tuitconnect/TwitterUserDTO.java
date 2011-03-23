package interfaces.tuitconnect;

public class TwitterUserDTO {
	public Long userId;
	public String screenName;
	public String token;
	public String tokenSecret;
	
	public TwitterUserDTO(Long userId, String screenName, String token, String tokenSecret) {
		this.userId = userId;
		this.screenName = screenName;
		this.token = token;
		this.tokenSecret = tokenSecret;
	}
}
