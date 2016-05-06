public class ShoppingEvent implements Comparable<ShoppingEvent>{
	public enum EventType {
		CUSTOMER_ARRIVAL, PROCESS_ITEM;
	}

	private EventType eventType;
	private double eventTime;
	private int eventID;

	public ShoppingEvent(EventType eType, double eTime, int eventID)
	{
		eventType = eType;
		eventTime = eTime;
		this.eventID = eventID;
	}

	public double eventTime()
	{
		return eventTime;
	}

	public EventType eventType()
	{
		return eventType;
	}

	public int eventID()
	{
		return eventID;
	}

	public int compareTo(ShoppingEvent that)
	{
		if (eventTime() < that.eventTime())
			return -1;
		if (eventTime() > that.eventTime())
			return 1;
		return 0;
	}
}