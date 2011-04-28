package controllers.tuitconnect;

import java.util.HashMap;
import java.util.Map;

import play.Play;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Router;
import socialconnector.SocialPlatformAuthenticationHandler;
import socialconnector.SocialPlatformConnector;
import tuitconnect.TuitService;
import tuitconnect.TwitterAccount;
import tuitconnect.TwitterAuthenticationHandler;
import twitter4j.Twitter;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

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

	// This interceptor is activated when using TwitterConnector directly;
	// ignored if behind SocialConnector
	@Before
	static void interceptor() throws Exception {
		// who is handling my callback?
		Class handler = Play.classloader.getAssignableClasses(
				TwitterAuthenticationHandler.class).get(0);
		interceptor((TwitterAuthenticationHandler) handler.newInstance());
	}

	// The authorization flow interceptor
	static void interceptor(SocialPlatformAuthenticationHandler callback) throws Exception {
		if (inProcess()) {
			endAuth(params.get(OAUTH_TOKEN), params.get(OAUTH_VERIFIER),
					callback);
		} else {
			beginAuth();
		}
	}

	// This action is never called because the interceptor catches the flow
	// always
	public static void index() {
	}

	static Boolean inProcess() {
		return flash.contains(AUTH_PROCESS);
	}

	static void beginAuth() throws Exception {
		// TODO: the TwitterModule shouldn't know about the implementation of
		// SocialConnector controller
		// we are adding ?platform=twitter just for the SocialConnector.Index()
		// to get it after Twitter oauth callback
		// plus if we are using this module directly (without SocialConnector),
		// sending this param makes no sense
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("platform", "twitter");

		String callbackUrl = Router.getFullUrl(request.action, args);
		RequestToken requestToken = TuitService.getRequestToken(callbackUrl);

		flash.put(AUTH_PROCESS, 1);
		flash.put(OAUTH_TOKEN, requestToken.getToken());
		flash.put(OAUTH_SECRET, requestToken.getTokenSecret());

		redirect(requestToken.getAuthenticationURL());
	}

	static void endAuth(String oauth_token, String oauth_verifier,
			SocialPlatformAuthenticationHandler callback) throws Exception {
		Twitter twitter = TuitService.twitterFactory();
		
		RequestToken requestToken = new RequestToken(flash.get(OAUTH_TOKEN), flash.get(OAUTH_SECRET));
		AccessToken accessToken = twitter
				.getOAuthAccessToken(requestToken, oauth_verifier);

		TwitterAccount account = new TwitterAccount(
				accessToken.getUserId(),
				accessToken.getToken(), accessToken.getTokenSecret());

		callback.authenticationSuccess(account);
	}
}