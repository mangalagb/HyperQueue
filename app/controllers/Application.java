package controllers;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import models.*;
import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import views.html.*;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.With;

import java.math.BigInteger;

/**
 * @author gowri 
 * Controller to implement the HyperQueue broker.
 */

public class Application extends Controller {

	// There are 3 topics for this application - flowers, animals and birds

	// for topic category "flowers"
	LinkedList<Event> queue1 = new LinkedList<Event>();

	// for topic category "animals"
	LinkedList<Event> queue2 = new LinkedList<Event>();

	// for topic category "birds"
	LinkedList<Event> queue3 = new LinkedList<Event>();

	// Data structure to store session ids
	// and the user's progress through the topics.
	Hashtable<String, TopicQueuePosition> sessionId = new Hashtable<String, TopicQueuePosition>();
	
	//Variable to store the time when a request was made by a consumer.
	//Used for calculating the timeout value of a session.
	static long userSessionDurationTime;

	/**
	 * Gets the topic from the url and opens a page for the user to enter the
	 * message
	 * 
	 * @param topic : Topic the user is interested in
	 * @return a view that allows the user to enter the message for the event
	 */
	public Result extractTopic(String topic) {

		return ok(entermessage.render(topic));

	}

	/**
	 * Creates an Event based on the user entered message and adds it to the
	 * appropriate queue
	 * 
	 * @return a page showing that the event has been added to the topic queue.
	 */
	public Result createEvent() {
		DynamicForm dynamicForm = Form.form().bindFromRequest();
		String eventMessage = dynamicForm.get("eventMessage");
		String topic = dynamicForm.get("topicName");

		Event event = new Event();
		event.setMessage(eventMessage);
		event.setTopic(topic);

		if (topic.equals("flowers")) {
			queue1.add(event);
		} else if (topic.equals("animals")) {
			queue2.add(event);
		} else if (topic.equals("birds")) {
			queue3.add(event);
		}
		return ok(eventAdded.render(topic));

	}

	/**
	 * Function that implements consumer functionality. Keeps track of user
	 * session id or assigns a new one if user is visiting for the first time.
	 * Based on the session id, it retrieves appropriate events from the queue.
	 * 
	 * @param topic
	 * @return a view that displays the event or a session timeout message
	 */
	public Result displayEvent(String topic) {

		// Get the value associated with the "connected" cookie
		// The value is the session id
		String currentSessionId = session("connected");

		// If the sessionId is already present in the HashTable
		if (currentSessionId != null && sessionId.containsKey(currentSessionId)) {

			// Add current time to the session time value.
			long updatedTime = System.currentTimeMillis()
					- userSessionDurationTime;
			System.out.println("Updated time is : " + updatedTime);

			// To change the session duration time, change this equation.
			// The system time is in milliseconds.
			// This equation evaluates to 30,000 milliseconds ie. 30 seconds
			if (updatedTime < 0.5 * 60 * 1000) {
				System.out.println("Welcome back. session id :  "
						+ currentSessionId + "pos :  "
						+ sessionId.get(currentSessionId).showPositions());
			} else {
				// If session duration time has passed, clear the current
				// session,
				// display a message and reset the counter.
				// To start a new session, type the url again.
				session().clear();
				userSessionDurationTime = System.currentTimeMillis();
				return ok("Bye. Your session has timed out (Default session time = 30 seconds). Type the url again to start a new session");

			}

		}

		else {

			userSessionDurationTime = System.currentTimeMillis();

			System.out.println("New time is : " + userSessionDurationTime);

			SecureRandom random = new SecureRandom();
			String s = new BigInteger(130, random).toString(32);

			session("connected", s);

			TopicQueuePosition position = new TopicQueuePosition();
			position.setQueue1Position(0);
			position.setQueue2Position(0);
			position.setQueue3Position(0);

			sessionId.put(s, position);

			currentSessionId = session("connected");

			System.out.println("New session id :  " + currentSessionId
					+ " pos :  "
					+ sessionId.get(currentSessionId).showPositions());

		}

		// search queue for appropriate event to display
		Event event = new Event();

		if (topic.equals("flowers") && !queue1.isEmpty()) {
			TopicQueuePosition currentPosition = sessionId
					.get(currentSessionId);
			int queue1Position = currentPosition.getQueue1Position();

			event = queue1.get(queue1Position);

			// Update the new position of the user after viewing the event
			currentPosition.setQueue1Position(queue1Position + 1);

			// Update the HashTable to reflect the new position
			sessionId.put(currentSessionId, currentPosition);

		} else if (topic.equals("animals") && !queue2.isEmpty()) {
			TopicQueuePosition currentPosition = sessionId
					.get(currentSessionId);
			int queue2Position = currentPosition.getQueue2Position();

			event = queue2.get(queue2Position);

			// Update the new position of the user after viewing the event
			currentPosition.setQueue2Position(queue2Position + 1);

			// Update the HashTable to reflect the new position
			sessionId.put(currentSessionId, currentPosition);
		} else if (topic.equals("birds") && !queue3.isEmpty()) {
			TopicQueuePosition currentPosition = sessionId
					.get(currentSessionId);
			int queue3Position = currentPosition.getQueue3Position();

			event = queue3.get(queue3Position);

			// Update the new position of the user after viewing the event
			currentPosition.setQueue3Position(queue3Position + 1);

			// Update the HashTable to reflect the new position
			sessionId.put(currentSessionId, currentPosition);
		}

		return ok(displayevent.render(event));
	}
}
