package controllers;

import controllers.tuitconnect.*;
import interfaces.tuitconnect.*;
import play.*;
import play.mvc.*;
import play.cache.*; 

public class Application extends Controller implements TwitterAuthenticationHandler {
	@Override
	public void authenticationFail() {
		// TODO Auto-generated method stub
	}

	@Override
	public void authenticationSuccess(TwitterUserDTO user) {
		setSession(user.userId);
		Cache.set("screenName", user.screenName);
		home();
	}
	
	public static void index() {
		render();
	}
	
	public static void home() {
		renderArgs.put("screenName", Cache.get("screenName"));
		render();
	}
	
	public static void logout() {
		clearSession();
		index();
	}
	
	static void setSession(Long id) {
		session.put("tuid", id);
	}
	
	static void clearSession() {
		session.clear();
	}
}
