package controllers.tuitconnect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import annotations.tuitconnect.TwitterCallback;
import groovy.ui.text.FindReplaceUtility;
import interfaces.tuitconnect.*;
import play.Play;
import play.db.jpa.Model;
import play.mvc.*;
import play.utils.Java;
import services.tuitconnect.*;
import socialconnector.SocialPlatformAuthenticationHandler;
import socialconnector.SocialPlatformConnector;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.http.AccessToken;
import twitter4j.http.RequestToken;

public class TwitterConnector extends Controller implements SocialPlatformConnector {
	private static final String OAUTH_TOKEN = "oauth_token";
	private static final String OAUTH_VERIFIER = "oauth_verifier";
	private static final String OAUTH_SECRET = "oauth_secret";
	private static final String AUTH_PROCESS = "auth_process";
		
	// SocialConnector contract
	@Override
	public void handleAuthentication(SocialPlatformAuthenticationHandler callback) throws Exception {
		interceptor(callback);
	}

	// Direct use interceptor
	@Before
	static void interceptor() throws Exception {
		interceptor(
				(TwitterAuthenticationHandler)Play.classloader.getAssignableClasses(TwitterAuthenticationHandler.class).get(0).newInstance());
	}
	
	// The real deal
	static void interceptor(SocialPlatformAuthenticationHandler callback) throws Exception {
		if(inProcess()) {
			endAuth(params.get(OAUTH_TOKEN), params.get(OAUTH_VERIFIER), callback);
		}
		else {
			beginAuth();
		}
	}
	
	static Boolean inProcess() {
		return flash.contains(AUTH_PROCESS);
	}
	
	static void beginAuth() throws TwitterException {
		Map<String,Object> args = new HashMap<String,Object>();
		args.put("platform", "twitter");
		
		String callbackUrl = Router.getFullUrl(request.action, args);
		RequestToken requestToken = TuitService.getRequestToken(callbackUrl);
		
		flash.put(AUTH_PROCESS, 1);
		flash.put(OAUTH_TOKEN, requestToken.getToken());
		flash.put(OAUTH_SECRET, requestToken.getTokenSecret());

		redirect(requestToken.getAuthenticationURL());
	}

	static void endAuth(String oauth_token, String oauth_verifier, SocialPlatformAuthenticationHandler callback) throws Exception {
		Twitter twitter = TuitService.factory();
		AccessToken accessToken = twitter.getOAuthAccessToken(flash.get(OAUTH_TOKEN), flash.get(OAUTH_SECRET), oauth_verifier);

		TwitterAccount account = new TwitterAccount(
				Integer.toString(accessToken.getUserId()), accessToken.getToken(), accessToken.getTokenSecret());
//		user.profile.screenName = accessToken.getScreenName();

		callback.handleAuthenticationSuccess(account);
	}
	
	public static void index() {
	}
}
