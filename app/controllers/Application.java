package controllers;

import play.cache.Cache;
import play.mvc.Controller;
import socialconnector.SocialAccount;
import socialconnector.SocialConnectorAuthenticationHandler;
import socialconnector.exceptions.SocialProfileException;
import tuitconnect.TwitterAccount;

public class Application extends Controller implements
		SocialConnectorAuthenticationHandler {
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

	public static void home() throws SocialProfileException {
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
