package controllers.socialconnector;

import java.util.List;
import play.Play;
import play.mvc.Controller;
import socialconnector.SocialConnectorAuthenticationHandler;
import socialconnector.SocialPlatformAuthenticationHandler;
import socialconnector.SocialPlatformConnector;
import socialconnector.account.SocialAccount;
import twitter4j.TwitterException;

public class SocialConnector extends Controller implements SocialPlatformAuthenticationHandler {
	public static void index(String platform) throws Exception {
		// TODO: cache this resolution
		List<Class> connectorClasses = Play.classloader.getAssignableClasses(SocialPlatformConnector.class);
		
		SocialPlatformConnector connector = null;
		String className = platform + "connector";
		for(Class connectorClass : connectorClasses) {
			if(connectorClass.getSimpleName().equalsIgnoreCase(className)) {
				connector = (SocialPlatformConnector)connectorClass.newInstance();
				break;
			}
		}
		if(connector == null) throw new ClassNotFoundException(className);
		connector.handleAuthentication(new SocialConnector());
	}

	@Override
	public void authenticationSuccess(SocialAccount account) throws InstantiationException, IllegalAccessException, IllegalStateException, TwitterException {
		getHandler().authenticationSuccess(account);
	}

	@Override
	public void authenticationFail() throws InstantiationException, IllegalAccessException {
		getHandler().authenticationFail();
	}
	
	private SocialConnectorAuthenticationHandler<SocialAccount> getHandler() throws InstantiationException, IllegalAccessException {
		List<Class> classes = Play.classloader.getAssignableClasses(SocialConnectorAuthenticationHandler.class);
		return (SocialConnectorAuthenticationHandler)classes.get(0).newInstance();
	}
	
}
