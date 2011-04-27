package tuitconnect;

import java.io.Serializable;
import socialconnector.SocialProfile;

public class TwitterProfile extends SocialProfile implements Serializable {
	public Integer followers;
	public Integer following;
}
