
/*************************************************************************
 * Name: 
 * NetID: 
 * Precept:
 *
 * Dependencies:
 * Description: CheckoutLine template
 *  
 *************************************************************************/

import java.awt.Color;

public class CheckoutLine {
	/* Data structure of your choice here */ customersInLine;
	double checkerSpeed;

	public CheckoutLine(double checkerSpeed)
	{
		/* You should initialize your data structure here */
		this.checkerSpeed = checkerSpeed;
	}

	// Generates an exponentially distributed random variable
	// that tracks the amount of time until the next item will
	// be processed by this checker.

	public double timeToProcessNextItem() 
	{
		return -Math.log(1 - StdRandom.uniform()) / checkerSpeed;		
	}

	// Cases:
	// 1. There are no customers in line.
	//    In this case you should return null.

	// 2: The customer who has been in line the longest has 2 or more items.
	//    This customer's item count should be reduced by 1.
	//    The number and order of the customers should not change.
	//    Return the customer who has been in line the longest.

	// 3: The customer who has been in line the longest has 1 item.
	//    This customer's item count should be reduced to 0.
	//    This customer should be removed from the line.
	//    Return the customer who has left the line.

	public Customer processItem()
	{
	}

	// Adds a customer to the line. It is important to make sure
	// you can somehow track the order of customers in order for the
	// processItem method to work properly.

	public void getInLine(Customer newCustomer)
	{
	}	

	// Remove the last customer in the line, and return him.

	public Customer leaveLine()
	{
	}

	// Returns the number of customers in the line.

	public int length()
	{
	}

	// Draws the line. YOu do not need to modify this method
	// unless you changed the name of the customersInLine variable.

	public void draw(int thisLine, int totalLines)
	{
		int maxYshow = 15;
		double xSpaceBetween = 1.0 / (totalLines + 1);
		double ySpaceBetween = 0.9 / maxYshow;
		double bottomX = xSpaceBetween * (thisLine + 1);
		double bottomY = 0.05;

		int i = 0;

		int MAX_DRAW = 13;
		int drawn = 0;
		for (Customer c : customersInLine)
		{
			double thisY = bottomY + ySpaceBetween * i++;
			c.draw(bottomX, thisY, 0.003);
			drawn++;
			if (drawn >= MAX_DRAW)
				break;
		}

	}
}