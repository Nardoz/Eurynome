package socialconnector.profile;

import java.net.URL;

import play.db.jpa.GenericModel;

public class SocialProfile {
	public URL url;
	public String screenName;
	public URL pictureUrl;
	public String firstName;
	public String lastName;
	public String description;
	public String location;
	public String language;
	public Integer connectionsCount; 
	
	@Override
	public String toString() {
		return this.screenName;
	}
}
