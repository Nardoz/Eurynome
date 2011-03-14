package controllers.tuit;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Future;

import jobs.tuit.UserTimelineJob;

import models.tuit.TuitAccount;
import models.tuit.TuitUser;
import play.Logger;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Router;
import play.mvc.WebSocketController;
import services.tuit.TuitService;
import services.tuit.UserService;
import twitter4j.DirectMessage;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.http.AccessToken;
import twitter4j.http.RequestToken;

public class TuitController extends Controller {

	private static Boolean signedin = false;
	
	public static void test() {
		render();
	}
	
	@Before
	public static void setSessionStatus() {
		signedin = Boolean.parseBoolean(session.get("signedin"));
	}

	@Before(unless = { "callback", "signin" })
	public static void addToView() {
		renderArgs.put("signedin", signedin);
	}

	@Before(unless = { "index", "callback", "signin" })
	public static void checkSessionAndRedirect() {
		if (!signedin) {
			redirect("tuit.TuitController.index");
		}
	}

	public static void index() {

		if (session.get("accountId") != null) {
			List accounts = TuitAccount.findAll();
			TuitAccount account = TuitAccount.findById(Long.parseLong(session.get("accountId")));
			Long accountId = account.userId;
			String screenName = account.screenName;

			render(accountId, screenName, accounts);
		} else {
			render();
		}
	}

	public static void postDM(Long id, String screenName, String text) {
		TuitAccount account = TuitAccount.findById(id);
		
		if (account == null) {
			error("User.id not found: " + id);
		}
		
		Twitter twitter = TuitService.factory(account);

		try {
			DirectMessage sendDirectMessage = twitter.sendDirectMessage(screenName, text);
			flash.put("message", sendDirectMessage.getId());
			redirect("tuit.TuitController.timeline", id);

		} catch (TwitterException e) {
			Logger.error(e.getMessage());
			error(e);
		}
	}
	
	public static void timeline() {
		render();
	}
	
	public static class TimelineSocket extends WebSocketController {
		
		public static void getTimeline(Long id) {
			Future<InputStream> user = new UserTimelineJob(id).now();
			
			while(inbound.isOpen()) {				
		        outbound.sendJson(await(user));	
			}
		}
	}

	public static void signin() {
		try {
			String callbackUrl = Router.getFullUrl("tuit.TuitController.callback");
			RequestToken requestToken = TuitService.getRequestToken(callbackUrl);

			session.put("requestToken_token", requestToken.getToken());
			session.put("requestToken_secret", requestToken.getTokenSecret());

			redirect(requestToken.getAuthenticationURL());

		} catch (TwitterException e) {
			Logger.error(e, e.toString(), "");
			error(e.getMessage());

		}
	}

	public static void signout() {
		signedin = false;
		session.clear();
		redirect("tuit.TuitController.index");
	}

	public static void callback(String oauth_token, String oauth_verifier) {
		Long id = -1L;

		try {
			Twitter twitter = TuitService.factory();

			AccessToken accessToken = twitter.getOAuthAccessToken(session.get("requestToken_token"), session.get("requestToken_secret"),
				oauth_verifier);

			String screenName = accessToken.getScreenName();

			TuitAccount existingUser = TuitAccount.find("byScreenName", screenName).first();

			if (existingUser == null) {
				TuitAccount user = new TuitAccount(new Long(accessToken.getUserId()), screenName, accessToken.getToken(), accessToken
					.getTokenSecret());
				user.save();
				id = user.userId;
			} else {
				id = existingUser.userId;
			}
			
			signedin = true;
			session.put("signedin", true);
			session.put("accountId", id);

		} catch (TwitterException e) {
			Logger.error(e, "");
		}

		redirect("tuit.TuitController.index");
	}
}
