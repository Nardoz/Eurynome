package controllers;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import annotations.TwitterCallback;
import groovy.ui.text.FindReplaceUtility;
import interfaces.TwitterAuthenticationHandler;
import interfaces.TwitterUserDTO;
import play.Play;
import play.db.jpa.Model;
import play.mvc.*;
import play.utils.Java;
import services.TuitService;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.http.AccessToken;
import twitter4j.http.RequestToken;

public class TwitterAware extends Controller {
	protected static String callbackController;

	@Before(priority=1)
	static void checkSession() throws TwitterException {
		if(!session.contains("tuid") && !flash.contains("auth_process")) {
			beginAuth();
		}
	}
	
	@Before(priority=5)
	static void setSession() throws TwitterException, Exception {
		if(flash.contains("auth_process")) {
			String oauth_token = params.get("oauth_token");
			String oauth_verifier = params.get("oauth_verifier");

			endAuth(oauth_token, oauth_verifier);
		}
	}
	
	static void beginAuth() throws TwitterException {
		String callbackUrl = Router.getFullUrl(request.action);
		RequestToken requestToken = TuitService.getRequestToken(callbackUrl);
		
		flash.put("auth_process", "1");
		flash.put("requestToken_token", requestToken.getToken());
		flash.put("requestToken_secret", requestToken.getTokenSecret());

		redirect(requestToken.getAuthenticationURL());
	}

	static void endAuth(String oauth_token, String oauth_verifier) throws Exception {
		Twitter twitter = TuitService.factory();

		AccessToken accessToken = twitter.getOAuthAccessToken(flash.get("requestToken_token"), flash.get("requestToken_secret"), oauth_verifier);
		String screenName = accessToken.getScreenName();
		Long userId = new Long(accessToken.getUserId());
		String token = accessToken.getToken();
		String tokenSecret = accessToken.getTokenSecret();

		session.put("tuid", userId); 
		
//		List<Method> methods = Java.findAllAnnotatedMethods(request.controllerClass, TwitterCallback.class);
		List<Class> classes = Play.classloader.getAssignableClasses(TwitterAuthenticationHandler.class);
		
		TwitterUserDTO user = new TwitterUserDTO(userId, screenName, token, tokenSecret);
		
		TwitterAuthenticationHandler handler = (TwitterAuthenticationHandler)classes.get(0).newInstance();
		handler.authenticationSuccess(user);
	}
}
