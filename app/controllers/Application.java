package controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import models.*;
import play.data.DynamicForm;
import play.data.Form;
import views.html.*;
import play.mvc.Controller;
import play.mvc.Result;


public class Application extends Controller {

    
    public Result displayHomePage()
    {
    	
    	String message = "Hello World!";
    	
    	return ok(homepage.render(message));
    }


    public Result roleSelector() throws IOException
    {
	 // Get the submitted form data from the request object, and run validation.
	DynamicForm dynamicForm = Form.form().bindFromRequest();
	
	String role = dynamicForm.get("Role");
	
		if(role.equals("Producer"))
		{
			System.out.println(role);
			return ok(producer.render(role));
			
		}
		else
			return ok(consumer.render("i am a consumer"));
    }
    
    public Result setEvent() throws IOException
    {
    	DynamicForm dynamicForm = Form.form().bindFromRequest();
    	
    	String myEvent = dynamicForm.get("eventMessage");
    	
    	Queue<Event> myQueue = new LinkedList<Event>();
    	
    	Event ev = new Event();
    	ev.setMessage(myEvent);
    	
    	myQueue.add(ev);
    	
    	return ok(general.render(myEvent));
    }

}
