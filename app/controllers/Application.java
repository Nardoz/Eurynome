package controllers;

import models.tuitconnect.TuitAccount;
import play.cache.Cache;
import play.mvc.Controller;
import socialconnector.SocialAccount;
import socialconnector.SocialConnectorAuthenticationHandler;
import socialconnector.SocialProfile;
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

		SocialProfile profile = new SocialProfile();
		
		try {
			profile = account.getProfile();
		} catch(SocialProfileException e) { 
			error(e.getMessage());
		}
		
		// TODO: should detect which platform was chosen by the user
		if(TuitAccount.count("twitterId", Long.parseLong(account.userId)) == 0)
		{
			TuitAccount cachedAccount = new TuitAccount();
			cachedAccount.twitterId = Long.parseLong(account.userId);
			cachedAccount.locale = profile.locale;
			cachedAccount.screenName = profile.nickname;
			cachedAccount.token = account.token;
			cachedAccount.tokenSecret = account.tokenSecret;
			cachedAccount.save();
		}
		
		// Serialize account to use latter
		Cache.set("account-" + account.userId, account);
		Cache.set("profile-" + account.userId, profile);

		redirect("Application.home");
	}

	@Override
	public void authenticationFail() {
		error("authenticationFail");
	}

	public static void home() throws SocialProfileException {
		String userId = session.get("account-userid");
		TwitterAccount account = (TwitterAccount) Cache.get("account-"+userId);

		renderArgs.put("profile", Cache.get("profile-"+userId));
		render();
	}

	public static void logout() {
		session.clear();
		index();
	}
}
