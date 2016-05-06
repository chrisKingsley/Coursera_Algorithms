// Used to log customer data. Only the last windowSize customers are stored.

// This class is NOT efficient! 
import java.awt.Font;
import java.awt.Color;

public class ShoppingDataLog {

	int windowSize;
	Queue<Customer> customers;
	int queueSize;
    private static final Font courier = new Font("Courier", Font.PLAIN, 16);


	public ShoppingDataLog(int windowSize)
	{

		this.windowSize = windowSize;
		queueSize++;
		customers = new Queue<Customer>();
	}

	public void add(Customer c)
	{
		queueSize++;

		customers.enqueue(c);
		if (customers.size() > windowSize)
			customers.dequeue();

	}

	// This method could be reduced to constant time with a better data structure

	public double maxTimeTaken()
	{
		double maxValue = Double.NEGATIVE_INFINITY;
		for (Customer c : customers)
			if (c.timeTaken() > maxValue)
				maxValue = c.timeTaken();

		return maxValue;
	}


	public Color slowestCheckerColor()
	{
		double maxValue = Double.NEGATIVE_INFINITY;
		Color slowestColor = StdDraw.WHITE;

		for (Customer c : customers)
			if (c.timeTaken() > maxValue)
			{
				maxValue = c.timeTaken();
				slowestColor = c.color();
			}

		return slowestColor;
	}

	// This method could be reduced to constant time with a better data structure

	public double avgTime()
	{
		double maxValue = Double.NEGATIVE_INFINITY;
		double timeSum = 0;
		for (Customer c : customers)
			timeSum += c.timeTaken();

		return timeSum / windowSize;
	}

	// Information in the log is added to the screen

	public void draw(double x, double y, double increment)
	{
		double mTT = maxTimeTaken();
		
		StdDraw.setPenColor(StdDraw.GRAY);
		StdDraw.textLeft(x, y, "Max customer time: ");
		StdDraw.setPenColor(slowestCheckerColor());			
		StdDraw.textLeft(x + 0.4, y, String.format("%4.2fs", mTT));
		StdDraw.setPenColor(StdDraw.GRAY);
		StdDraw.textLeft(x + 0.54, y, String.format(", average; %4.2fs", avgTime()));

		StdDraw.setPenRadius(0.008);

		for (Customer c : customers)
		{
			StdDraw.setPenColor(c.color());
			StdDraw.point(c.timeTaken() / mTT, 0.95);
		}

		StdDraw.setPenRadius();
	}
}