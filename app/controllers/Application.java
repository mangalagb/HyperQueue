package controllers;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Hashtable;
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

public class Application extends Controller {
	
	//for topic category "flowers"
	LinkedList<Event> queue1 = new LinkedList<Event>(); 
	
	//for topic category "animals"
	LinkedList<Event> queue2 = new LinkedList<Event>();
	
	//for topic category "birds"
	LinkedList<Event> queue3 = new LinkedList<Event>();
	
	Hashtable<String,TopicQueuePosition> sessionId = new Hashtable<String,TopicQueuePosition>();
    
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
    	return ok(addMoreEvents.render(topic));
		
	}

	public Result displayEvent(String topic)
	{
		String currentSessionId = session("connected");
		
    	if(currentSessionId != null && sessionId.containsKey(currentSessionId))
    	{
    		System.out.println("Welcome back. session id :  "+currentSessionId+
    				"pos :  "+sessionId.get(currentSessionId).showPositions());
    	}
    	else
    	{
    		SecureRandom random = new SecureRandom();
    		String s = new BigInteger(130, random).toString(32);
        	
        	session("connected", s);
        	
        	TopicQueuePosition position = new TopicQueuePosition();
        	position.setQueue1Position(0);
        	position.setQueue2Position(0);
        	position.setQueue3Position(0);
        	
        	sessionId.put(s, position);
        	
        	currentSessionId = session("connected");
       
        	System.out.println("New session id :  "+currentSessionId+" pos :  "+sessionId.get(currentSessionId).showPositions());
    	}
    	
    	// search queue for appropriate event to display
    	Event event = new Event();
    	
    	if(topic.equals("flowers") && !queue1.isEmpty())
    	{
    		TopicQueuePosition currentPosition = sessionId.get(currentSessionId);
    		int queue1Position = currentPosition.getQueue1Position();
    		
    		event = queue1.get(queue1Position);
    		
    		//Update the new position of the user after viewing the event
    		currentPosition.setQueue1Position(queue1Position+1);
    		
    		//Update the HashTable to reflect the new position
    		sessionId.put(currentSessionId, currentPosition);
    		
    	}
    	else if(topic.equals("animals") && !queue2.isEmpty())
    	{
    		TopicQueuePosition currentPosition = sessionId.get(currentSessionId);
    		int queue2Position = currentPosition.getQueue2Position();
    		
    		event = queue2.get(queue2Position);
    		
    		//Update the new position of the user after viewing the event
    		currentPosition.setQueue2Position(queue2Position+1);
    		
    		//Update the HashTable to reflect the new position
    		sessionId.put(currentSessionId, currentPosition);
    	}
    	else if(topic.equals("birds") && !queue3.isEmpty())
    	{
    		TopicQueuePosition currentPosition = sessionId.get(currentSessionId);
    		int queue3Position = currentPosition.getQueue3Position();
    		
    		event = queue3.get(queue3Position);
    		
    		//Update the new position of the user after viewing the event
    		currentPosition.setQueue3Position(queue3Position+1);
    		
    		//Update the HashTable to reflect the new position
    		sessionId.put(currentSessionId, currentPosition);
    	}
    	
    	return ok(displayevent.render(event));
	}
}
