package models;

public class TopicQueuePosition
{
	int queue1Position;
	int queue2Position;
	int queue3Position;
	
	public void setQueue1Position(int i)
	{
		queue1Position = i;
	}
	
	public void setQueue2Position(int i)
	{
		queue2Position = i;
	}
	
	public void setQueue3Position(int i)
	{
		queue3Position = i;
	}
	
	public int getQueue1Position()
	{
		return queue1Position;
	}
	
	public int getQueue2Position()
	{
		return queue2Position;
	}
	
	public int getQueue3Position()
	{
		return queue3Position;
	}
	
	public String showPositions()
	{
		String s = Integer.toString(queue1Position) + Integer.toString(queue2Position) + Integer.toString(queue3Position);
		return s;
	}
}