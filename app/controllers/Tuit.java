package controllers;

import java.util.List;

import models.User;
import play.Logger;
import play.Play;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Router;
import play.mvc.results.Redirect;
import services.TwitterConnect;
import twitter4j.DirectMessage;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.http.AccessToken;
import twitter4j.http.RequestToken;

public class Tuit extends Controller {
	
	private static boolean loggedin = false;

	@Before(unless={"callback", "signin"})
	public static void checkSession() {
		
		if(session.get("loggedin") != null && session.get("loggedin").equals("1")) {
			loggedin = true;
		} 
		
		renderArgs.put("loggedin", loggedin);
	}
	
	@Before(unless={"index", "callback", "signin"})
	public static void checkSessionAndRedirect() {
		if(!loggedin) {
			redirect("Tuit.index");
		}
	}
	
	public static void index() { 
		List users = User.findAll();
		render(users);
	}

	public static void postDM(Long id, String screenName, String text) {
		
		User user = User.findById(id);
		
		if(user == null) {
			error("User.id not found: " + id);
		}
		
		Twitter twitter = TwitterConnect.factory(user);

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
		
		User user = User.findById(id);
		
		if(user == null) {
			error("User.id not found: " + id);
		}
			
		Twitter twitter = TwitterConnect.factory(user);

		try {
			// return max allowed from Twitter
			Paging paging = new Paging(1, 100);

			ResponseList<Status> timeline = twitter.getUserTimeline(paging);
			ResponseList<Status> mentions = twitter.getMentions(paging);

			render(user, mentions, timeline);

		} catch (TwitterException e) {
			Logger.error(e, e.toString(), "");
			error(e.getMessage());
		}
	}

	public static void signin() {
		try {
			RequestToken requestToken = TwitterConnect.getRequestToken();

			session.put("requestToken_token", requestToken.getToken());
			session.put("requestToken_secret", requestToken.getTokenSecret());
			session.put("loggedin", 1);

			redirect(requestToken.getAuthenticationURL());

		} catch (TwitterException e) {
			Logger.error(e, e.toString(), "");
			error(e.getMessage());

		}
	}

	public static void callback(String oauth_token, String oauth_verifier) {
		Long id = -1L;
		
		try {
			Twitter twitter = TwitterConnect.factory();
			
			AccessToken accessToken = twitter.getOAuthAccessToken(
				session.get("requestToken_token"), 
				session.get("requestToken_secret"), 
				oauth_verifier
			);

			String screenName = accessToken.getScreenName();
			
			User existingUser = User.find("byScreenName", screenName).first();
			
			if(existingUser == null) {
				User user = new User(screenName, accessToken.getToken(), accessToken.getTokenSecret());
				user.save();
				id = user.id;
			} else {
				id = existingUser.id;
			}
			
			session.put("userId", id);

		} catch (TwitterException e) {
			Logger.error(e, "");
		}
		
		redirect("Tuit.timeline", id);
	}
}
