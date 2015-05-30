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
import play.mvc.With;

//@With(HttpsAction.class)
public class Application extends Controller {
	
	//for topic category "flowers"
	Queue<Event> queue1 = new LinkedList<Event>(); 
	
	//for topic category "animals"
	Queue<Event> queue2 = new LinkedList<Event>();
	
	//for topic category "birds"
	Queue<Event> queue3 = new LinkedList<Event>();
    
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
			return ok(producer.render());
			
		}
		else
			return ok(consumer.render());
    }
    
    public Result setEvent() throws IOException
    {
    	// For Producer
    	DynamicForm dynamicForm = Form.form().bindFromRequest();
    	
    	String eventMessage = dynamicForm.get("eventMessage");
    	String topic = dynamicForm.get("topic");
    	
    	Event event = new Event();
    	event.setMessage(eventMessage);
    	event.setTopic(topic);
    	
    	if(topic.equals("flowers"))
    	{
    		queue1.add(event);
    	}
    	else if(topic.equals("animals"))
    	{
    		queue2.add(event);
    	}
    	else if(topic.equals("birds"))
    	{
    		queue3.add(event);
    	}
    	return redirect("/");
    	
    }
    
    public Result getEvent() throws IOException
    {
    	//For Consumer
    	
    	DynamicForm dynamicForm = Form.form().bindFromRequest();
    	
    	String topic = dynamicForm.get("topic");
    	
    	
    	//implement queue searching based on topic here
    	Event event = new Event();
    	
    	if(topic.equals("flowers") && !queue1.isEmpty())
    	{
    		event = queue1.peek();
    	}
    	else if(topic.equals("animals") && !queue2.isEmpty())
    	{
    		event = queue2.peek();
    	}
    	else if(topic.equals("birds") && !queue3.isEmpty())
    	{
    		event = queue3.peek();
    	}
    
    	
    	return ok(displayevent.render(event));
    	
    }
    
    public Result showMainPage()
    {
    	return redirect("/");
    	
    }
    
//    public Result publishInFlowers() throws IOException
//    {
//    	return ok(newproducer.render());
//    }
//    
//    public Result addToFlowerQueue() throws IOException
//    {
//    	// For Producer
//    	DynamicForm dynamicForm = Form.form().bindFromRequest();
//    	
//    	String eventMessage = dynamicForm.get("eventMessage");
//    	String topic = "flowers";
//    	
//    	Event event = new Event();
//    	event.setMessage(eventMessage);
//    	event.setTopic(topic);
//
//    	queue1.add(event);
//    	
//    	return redirect("/");
//    	
//    }
}
