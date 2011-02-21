package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import play.mvc.*;

public class Utils extends Controller {

    public static void index() {
        render();
    }    

    public static void pull() {
    	String response = "";
    	
    	Runtime r = Runtime.getRuntime();
    	
    	try {
    		String line;    		
    		
    		r.exec("git checkout -f");    	
    		Process proc = r.exec("git pull");  
    		BufferedReader input = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			
    		while((line = input.readLine()) != null) {
				response += line;
			}
			
    		input.close();

		} catch (IOException e) { 
			response = "FAILED. " + e.getMessage();
		}
		
		renderText(response);
    }
    
    public static void tolocal(String oauth_token, String oauth_verifier) {
    	redirect("http://localhost:8080/tuit/callback/?oauth_token=" + oauth_token + "&oauth_verifier=" + oauth_verifier);
    }
}
