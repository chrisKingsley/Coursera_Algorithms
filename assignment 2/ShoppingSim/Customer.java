import java.awt.Color;

// This class provides a model of customer behavior, and tracks
// the information regarding a customer's trip through line.

public class Customer {
	private Color color;				   // The color is a semi-unique customer identifier
 										   // which represents the original line chosen
	private int numberOfItemsRemaining;    // Number of items left in basket
	private double arrivalTime;			   // Tracks the time the customer originally arrived
	private double timeTaken;			   // Tracks total time spent in line

	public Customer(int numItems, double arrivalTime)
	{
		color = null;
		this.numberOfItemsRemaining = numItems;
		this.arrivalTime = arrivalTime;
	}

	// Removes an item from customer's basket

	public void removeItem()
	{
		numberOfItemsRemaining--;
	}

	// returns true if no items left

	public boolean emptyBasket()
	{
		if (numberOfItemsRemaining == 0)
			return true;

		return false;
	}

	public void setColor(Color color)
	{
		this.color = color;
	}

	public Color color()
	{
		return color;
	}
	
	// used to track total time that the customer spent in line (somewhat flawed API here)

	public void setTimeTaken(double t)
	{
		timeTaken = t;
	}

	public double timeTaken()
	{
		return timeTaken;
	}

	// Given a set of lines, make a decision about which line to get in.
	// First a random line is chosen, and then nearby lines are considered.
	// The farther a line is from the initial choice, the shorter that
	// line must be relatively before the customer will select it.

	public int selectLine(CheckoutLine[] lines)
	{
		int randomLine = StdRandom.uniform(lines.length);
		int lineToJoin = getBestNearbyLine(lines, randomLine);

		return lineToJoin;
	}

	// Returns the 'best nearby line'. If no other aisle is shorter, then
	// the current line will be the best. If another line is i
	// people shorter, then that line will be best as long as it is within
	// i lines.

	// This method is used only in selecting the original line.

	private int getBestNearbyLine(CheckoutLine[] lines, int startLine)
	{
		int startLineLength = lines[startLine].length();
		int bestNearby = startLine;

		for (int i = 0; i < lines.length; i++)
		{
			int lineDistance = Math.abs(i - startLine);
			if (startLineLength - lines[i].length() >= i) //i lines away, i people shorter
			{	
				bestNearby = i;
			}
		}

		return bestNearby;
	}	

	// Determines whether or not the adjacent line is shorter by 2 people or more.
	// If so, then that line number is returned. Othewise, the current line number 
	// is returned.
	//
	// Used for line hopping after initial line selection.

	public int adjacentLineBetter(CheckoutLine[] lines, int currentLine)
	{
		int currentLineLength = lines[currentLine].length();
		int leftLineLength = Integer.MAX_VALUE;
		int rightLineLength = Integer.MAX_VALUE;
		int newLine = currentLine;

		if (currentLine > 0)
			leftLineLength = lines[currentLine - 1].length();

		if (currentLine < lines.length - 1)
			rightLineLength = lines[currentLine + 1].length();

		if (currentLineLength - leftLineLength > 2)
			newLine = currentLine - 1;

		if (currentLineLength - rightLineLength > 2)
			newLine = currentLine + 1;

		return newLine;
	}

	// returns the stored arrival time

	public double arrivalTime()
	{
		return arrivalTime;
	}

	// draws customer to screen

	public void draw(double x, double y, double widthPerItem)
	{
		StdDraw.setPenColor(color);
		
		StdDraw.filledSquare(x, y, numberOfItemsRemaining * widthPerItem);
	}

}