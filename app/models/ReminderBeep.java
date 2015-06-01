package models;

import models.TopicQueuePosition;

import java.awt.Toolkit;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;



public class ReminderBeep {
  Toolkit toolkit;

  Timer timer;
  
  int count=0;

  public ReminderBeep(int seconds)
  {
    toolkit = Toolkit.getDefaultToolkit();
    timer = new Timer();
    timer.schedule(new RemindTask(),0, seconds * 1000);
  }

  class RemindTask extends TimerTask {
    public void run() {
    	
    	System.out.println("waiting");
      
      ++count;
      
      if(count == 10)
      {
    	  timer.cancel();
      }
     
    }
  }
}