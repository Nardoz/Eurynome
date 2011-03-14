package controllers.Docs;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class Docs extends Controller {	
    public static void index(String page) {    	
    	if(page == null) {
    		page = "index";
    	}
    	renderTemplate("Docs/" + page + ".html");
    }
}