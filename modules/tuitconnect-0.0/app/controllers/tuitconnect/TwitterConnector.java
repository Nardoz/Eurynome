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

	// This interceptor is activated when using TwitterConnector directly; ignored if behind SocialConnector
	@Before
	static void interceptor() throws Exception {
		// who is handling my callback?
		Class handler = Play.classloader.getAssignableClasses(TwitterAuthenticationHandler.class).get(0);
		
		interceptor((TwitterAuthenticationHandler)handler.newInstance());
	}

	// The authorization flow interceptor
	static void interceptor(SocialPlatformAuthenticationHandler callback) throws Exception {
		if (inProcess()) {
			endAuth(params.get(OAUTH_TOKEN), params.get(OAUTH_VERIFIER), callback);
		} else {
			beginAuth();
		}
	}

	// This action is never called because the interceptor catches the flow always
	public static void index() {
	}
	
	static Boolean inProcess() {
		return flash.contains(AUTH_PROCESS);
	}

	static void beginAuth() throws Exception {
		// TODO: the TwitterModule shouldn't know about the implementation of SocialConnector controller
		// we are adding ?platform=twitter just for the SocialConnector.Index() to get it after Twitter oauth callback
		// plus if we are using this module directly (without SocialConnector), sending this param makes no sense
		Map<String, Object> args = new HashMap<String, Object>();
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

		TwitterAccount account = new TwitterAccount(Integer.toString(accessToken.getUserId()), 
			accessToken.getToken(), accessToken.getTokenSecret());

		callback.authenticationSuccess(account);
	}
}
