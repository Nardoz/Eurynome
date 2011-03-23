package controllers;

import interfaces.*;
import annotations.*;
import play.*;
import play.mvc.*;
import play.cache.*; 

@With(TwitterAware.class)
public class Application extends Controller implements TwitterAuthenticationHandler {

	@Override
	public void authenticationFail() {
		// TODO Auto-generated method stub
	}

	@Override
	public void authenticationSuccess(TwitterUserDTO user) {
		Cache.set("screenName", user.screenName);
		home();
	}
	
	public static void index() {
		renderText("index");
	}
	
	public static void home() {
		renderText("hello " + Cache.get("screenName"));
	}
}
