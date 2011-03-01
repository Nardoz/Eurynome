package controllers;

import java.util.List;

import models.Account;
import models.UserDB;
import play.Logger;
import play.Play;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Router;
import play.mvc.results.Redirect;
import services.TwitterConnect;
import services.UserService;
import twitter4j.DirectMessage;
import twitter4j.IDs;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.http.AccessToken;
import twitter4j.http.RequestToken;

public class Tuit extends Controller {

	private static Boolean signedin = false;
	
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
			redirect("Tuit.index");
		}
	}

	public static void index() {

		if (session.get("accountId") != null) {
			List accounts = Account.findAll();
			Account account = Account.findById(Long.parseLong(session.get("accountId")));
			Long accountId = account.id;
			String screenName = account.screenName;

			render(accountId, screenName, accounts);
		} else {
			render();
		}
	}

	public static void postDM(Long id, String screenName, String text) {
		Account account = Account.findById(id);
		
		if (account == null) {
			error("User.id not found: " + id);
		}
		
		Twitter twitter = TwitterConnect.factory(account);

		try {
			DirectMessage sendDirectMessage = twitter.sendDirectMessage(screenName, text);
			flash.put("message", sendDirectMessage.getId());
			redirect("Tuit.timeline", id);

		} catch (TwitterException e) {
			Logger.error(e.getMessage());
			error(e);
		}
	}

	public static void timeline(Long id) {

		Account account = Account.findById(id);
		if (account == null)
			error("access.id not found: " + id);

		Twitter twitter = TwitterConnect.factory(account);

		try {
			UserDB me = UserService.find(account.userId);
			if (null == me) {
				User user = twitter.showUser(account.screenName);
				me = UserService.findOrCreate(user);
			}

			Paging paging = new Paging(1, 100);
			ResponseList<Status> mentions = twitter.getMentions(paging);
			ResponseList<Status> timeline = null;

			for (int i = 0; i < 15; i++) {
				paging = new Paging(i + 1, 100);
				timeline = twitter.getHomeTimeline(paging);

				if (timeline.size() == 0)
					break;

				// I'm following these users
				for (Status status : timeline) {
					User follower = status.getUser();
					UserDB follow2 = UserService.setFollowing(me, follower);
					
					
					// Reads followers information on each client.
					// TODO: Save list of UserId and lookup later

					// IDs followersIDs =
					// twitter.getFollowersIDs(follower.getId());
					// ResponseList<User> ff =
					// twitter.lookupUsers(followersIDs.getIDs());
					// for (User f : ff) {
					// UserService.setFollowing(follow2, f);
					// }

				}
			}

			// I can't establish relationship yet.
			// just save user information
			//
			for (Status status : mentions) {
				User user = status.getUser();
				UserService.findOrCreate(user);
			}

			renderArgs.put("screenName", account.screenName);
			render(mentions, timeline);

		} catch (TwitterException e) {
			Logger.error(e, e.toString(), "");
			error(e.getMessage());
		}
	}

	public static void signin() {
		try {
			String callbackUrl = Router.getFullUrl("Tuit.callback");
			RequestToken requestToken = TwitterConnect.getRequestToken(callbackUrl);

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
		redirect("Tuit.index");
	}

	public static void callback(String oauth_token, String oauth_verifier) {
		Long id = -1L;

		try {
			Twitter twitter = TwitterConnect.factory();

			AccessToken accessToken = twitter.getOAuthAccessToken(session.get("requestToken_token"), session.get("requestToken_secret"),
				oauth_verifier);

			String screenName = accessToken.getScreenName();

			Account existingUser = Account.find("byScreenName", screenName).first();

			if (existingUser == null) {
				Account user = new Account(new Long(accessToken.getUserId()), screenName, accessToken.getToken(), accessToken
					.getTokenSecret());
				user.save();
				id = user.id;
			} else {
				id = existingUser.id;
			}
			
			signedin = true;
			session.put("signedin", true);
			session.put("accountId", id);

		} catch (TwitterException e) {
			Logger.error(e, "");
		}

		redirect("Tuit.index");
	}
}
