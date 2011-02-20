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
}
