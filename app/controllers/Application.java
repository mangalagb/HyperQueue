package controllers;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import models.*;
import play.data.DynamicForm;
import play.data.Form;
import views.html.*;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.With;

import java.math.BigInteger;

//@With(HttpsAction.class)
public class Application extends Controller {
	
	//for topic category "flowers"
	Queue<Event> queue1 = new LinkedList<Event>(); 
	
	//for topic category "animals"
	Queue<Event> queue2 = new LinkedList<Event>();
	
	//for topic category "birds"
	Queue<Event> queue3 = new LinkedList<Event>();
    
	public Result extractTopic(String topic) 
	{
		
		return ok(entermessage.render(topic));
		
	}
	
	
	public Result createEvent() 
	{
		DynamicForm dynamicForm = Form.form().bindFromRequest();
    	String eventMessage = dynamicForm.get("eventMessage");
    	String topic = dynamicForm.get("topicName");
    	
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
    	return ok(general.render("Your event has been added to the topic"));
    	
    	//localhost:9443?topic="queue1",session_id=Number
		
	}

	public Result displayEvent(String topic)
	{
//		SecureRandom random = new SecureRandom();
//		String s = new BigInteger(130, random).toString(32);
//    	
//    	session("connected", s);
//    	
//    	String user = session("connected");
//    	System.out.println(user);
    	  
    	
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
}
