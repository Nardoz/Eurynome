package controllers;

import interfaces.tuitconnect.TwitterAccount;

import com.mysql.jdbc.log.Log;

import play.*;
import play.mvc.*;
import play.cache.Cache;
import twitter4j.TwitterException;

import socialconnector.SocialConnectorAuthenticationHandler;
import socialconnector.account.SocialAccount;
import socialconnector.exceptions.SocialAccountProfileException;

public class Application extends Controller implements SocialConnectorAuthenticationHandler {
	public static void index() {
		render();
	}

	@Override
	public void authenticationSuccess(SocialAccount account) {
		// my Session Id
		session.put("account-userid", account.userId);

		// Serialize account to use latter
		Cache.set("account-" + account.userId, account);

		redirect("Application.home");
	}

	@Override
	public void authenticationFail() {
		error("authenticationFail");
	}

	public static void home() throws SocialAccountProfileException {
		String cacheKey = "account-" + session.get("account-userid");
		TwitterAccount account = (TwitterAccount) Cache.get(cacheKey);

		renderArgs.put("profile", account.getProfile());

		render();
	}

	public static void logout() {
		session.clear();
		index();
	}
}
