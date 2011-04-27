package socialconnector;

import java.net.URL;

public class SocialProfile {
	public URL url;
	public String nickname;
	public String firstName;
	public String lastName;
	public URL photoUrl;
	public String location;
	public String locale;
	public Integer connectionsCount; 
	
	@Override
	public String toString() {
		return this.nickname;
	}
}
