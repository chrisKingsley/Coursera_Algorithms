import java.awt.Color;
import java.awt.Font;


public class ShoppingSim {
	private CheckoutLine[] checkoutLines;
	private MinPQ<ShoppingEvent> eventQueue; // list of future events

    // A set of 22 maximally distinct colors was proposed by Kenneth Kelly in 1965.
    // See http://eleanormaclure.files.wordpress.com/2011/03/colour-coding.pdf for more
	private static final int[] kellys = new int[]{0xFFB300, 0x803E75, 0xFF6800, 0xA6BDD7, 0xC10020, 0xCEA262, 0x817066, 0x007D34, 0xF6768E, 0x00538A, 0xFF7A5C, 0x53377A, 0xFF8E00, 0xB32851, 0xF4C800, 0x7F180D, 0x93AA00, 0x593315, 0xF13A13, 0x232C16};
    private static final Color[] colors = new Color[kellys.length];
    static {
        for (int i = 0; i < kellys.length; i++)
            colors[i] = new Color(kellys[i]);
    }

    private SimParameters sp; // simulation parameters
    private double T; // total simulation time 
    private static final Font courier = new Font("Courier", Font.PLAIN, 16);
    private ShoppingDataLog dataLog; // log of customer checkout history


    private static class SimParameters {
        int numLines;               // number of checkout checkoutLines
        double minCheckerSpeed;     // minimum allowable checker speed
        double maxCheckerSpeed;     // maximum allowable checker speed
        double customerRate;        // rate of customer arrival
        int maxItems;               // max number of items allowed in line
        double deltaT;              // size of time step for simulation
        double drawDelta;           // simulation time between renderings to screen
        int customerTrackCount;     // number of customers to track at once;

        SimParameters(String filename)
        {
            In in = new In(filename);
            in.readString();
            numLines = in.readInt(); in.readString();
            minCheckerSpeed = in.readDouble(); in.readString();
            maxCheckerSpeed = in.readDouble(); in.readString();
            customerRate = in.readDouble(); in.readString();
            maxItems = in.readInt(); in.readString();
            deltaT = in.readDouble(); in.readString();
            drawDelta = in.readDouble(); in.readString();
            customerTrackCount = in.readInt();
        }

        SimParameters(SimParameters sp)
        {
            this.numLines = sp.numLines;
            this.minCheckerSpeed = sp.minCheckerSpeed;
            this.maxCheckerSpeed = sp.maxCheckerSpeed;
            this.customerRate = sp.customerRate;
            this.maxItems = sp.maxItems;
            this.deltaT = sp.deltaT;
            this.drawDelta = sp.drawDelta;  
            this.customerTrackCount = sp.customerTrackCount;          
        }

    }

	public ShoppingSim(SimParameters sp) 
	{
		checkoutLines = new CheckoutLine[sp.numLines];
        this.sp = new SimParameters(sp);
        dataLog = new ShoppingDataLog(sp.customerTrackCount);
        T = 0;

		for (int i = 0; i < sp.numLines; i++)
			checkoutLines[i] = new CheckoutLine(StdRandom.uniform(sp.minCheckerSpeed, sp.maxCheckerSpeed));

		eventQueue = new MinPQ<ShoppingEvent>();

        for (int i = 0; i < checkoutLines.length; i++)
            eventQueue.insert(new ShoppingEvent(ShoppingEvent.EventType.PROCESS_ITEM, 0, i));

        eventQueue.insert(new ShoppingEvent(ShoppingEvent.EventType.CUSTOMER_ARRIVAL, 0, 0));
	}

    // returns a random color that is arithmetically based on the base color

	private static Color randomColor(Color baseColor, int maxDev)
	{
		int r = baseColor.getRed();
		int g = baseColor.getGreen();
		int b = baseColor.getBlue();

		int newR = r + StdRandom.uniform(-maxDev, maxDev);
		int newG = g + StdRandom.uniform(-maxDev, maxDev);
		int newB = b + StdRandom.uniform(-maxDev, maxDev);

		newR = (int) Math.max(Math.min(newR, 255), 0);
		newG = (int) Math.max(Math.min(newG, 255), 0);
		newB = (int) Math.max(Math.min(newB, 255), 0);

		return new Color(newR, newG, newB);
	}

    // converts a number of seconds to amore human readable format

    private static String secondsToTimeString(double T)
    {
        double seconds = T % 60;
        int minutes = (int) (T / 60) % 60;
        int hours = (int) (T / 3600) % 24;
        int days = (int) (T / 86400) % 365;

        return String.format("%4dd %4dh %4dm %8.3gs", days, hours, minutes, seconds);
    }

    // draws the current state of the simulation

	public void draw()
	{
        double PROMPT_X = 0.5;
        double PROMPT_Y = 0.85;
        double T_X = 0.6;
        double T_Y = 0;
        double DATALOG_X = 0;
        double DATALOG_Y = 0.9;

		StdDraw.clear(StdDraw.BLACK);

		for (int i = 0; i < checkoutLines.length; i++)
			checkoutLines[i].draw(i, checkoutLines.length);		

		StdDraw.setPenColor(StdDraw.GRAY);

        StdDraw.setFont();
		StdDraw.text(PROMPT_X, PROMPT_Y, 
                String.format("Customer arrival rate (change with +/-): %.2f", sp.customerRate));
        StdDraw.setFont(courier);
        StdDraw.text(T_X, T_Y, "T: " + secondsToTimeString(T));
        dataLog.draw(DATALOG_X, DATALOG_Y, 0.1);        

		StdDraw.show(5);
	}

    // finds time to next customer assuming that customer arrival is a Poisson process

	private static double timeToNextCustomer(double lambda) {
		return -Math.log(1 - StdRandom.uniform())/lambda;
	}

    // print debug queue (used for debugging)

	private void printEventQueue()
	{
		for (ShoppingEvent se : eventQueue)
			System.out.printf("%.4f ", se.eventTime());
		System.out.print("\r");

	}

	public void mainLoop()
	{

		double nextDraw = 0;

		StdDraw.clear(StdDraw.BLACK);
		StdDraw.setPenColor(StdDraw.GREEN);

		StdDraw.setXscale(0, 1);
		StdDraw.setYscale(0, 1);

		T = 0;

		while (true)
		{
			T = sp.deltaT + T;


            // events are executed at the first T such that T > eventTime.

			while ((eventQueue.size() > 0) && (eventQueue.min().eventTime() < T))
			{

                // get earliest event that must be handled (e.g. eventTime < T)
				
                ShoppingEvent nextEvent = eventQueue.delMin();

                // If a customer arrives, create them, have them select a line.
                // Assign a color that tracks where their shopping adventure began,
                // and then add them to the end of the checkoutLine.

                // Finally, generate a new event corresponding to the arrival of the next 
                // new customer.

				if (nextEvent.eventType() == ShoppingEvent.EventType.CUSTOMER_ARRIVAL)
				{
					int numItems = StdRandom.uniform(sp.maxItems)+1;

					Customer customer = new Customer(numItems, nextEvent.eventTime());

					int lineNum = customer.selectLine(checkoutLines);
					customer.setColor(colors[lineNum]);
					checkoutLines[lineNum].getInLine(customer);

                    // generate next arrival

					double deltaTime = timeToNextCustomer(sp.customerRate);
					double newCustomerTime = nextEvent.eventTime() + deltaTime;
					ShoppingEvent newEvent = new ShoppingEvent(ShoppingEvent.EventType.CUSTOMER_ARRIVAL,
												 newCustomerTime, 0);
					eventQueue.insert(newEvent);


				}

                // If a checker completes the processing of an item, then 
                // test to see if the customer whose item has been processed has an
                // empty basket. If so, then add the customer to the log of all
                // customers who have completed their shopping excursion.

                // If there were no customers in line, then don't add anybody to the log. 

                // Process any customers who might want to swap lines because adjacent
                // lines are better.

                // Finally, generate a new event corresponding to the next item processing
                // for this checkoutLine.

				if (nextEvent.eventType() == ShoppingEvent.EventType.PROCESS_ITEM)
				{
					int lineNumber = nextEvent.eventID();

					Customer customer = checkoutLines[lineNumber].processItem();

                    if ((customer != null) && customer.emptyBasket())
                    {
                        customer.setTimeTaken(T - customer.arrivalTime());
                        dataLog.add(customer);
                    }


                    // line hopping as described above

                    for (int i = 0; i < checkoutLines.length; i++)
                    {
                        if (checkoutLines[i].length() == 0)
                            continue;
                        Customer lastCustomer = checkoutLines[i].leaveLine();
                        int newLine = lastCustomer.adjacentLineBetter(checkoutLines, i);
                        checkoutLines[newLine].getInLine(lastCustomer);
                    }

                    // generate next item processing

                    double timeToNextItem = checkoutLines[lineNumber].timeToProcessNextItem();
					double newItemTime = nextEvent.eventTime() + timeToNextItem;

					ShoppingEvent newEvent = new ShoppingEvent(ShoppingEvent.EventType.PROCESS_ITEM,
												 newItemTime, lineNumber);

					eventQueue.insert(newEvent);
				
				}

			}

            // handle adjustment of customer arrival rate

			if (StdDraw.hasNextKeyTyped())
			{
				char nextKey = StdDraw.nextKeyTyped();
				if (nextKey == '+')
					sp.customerRate += 0.01;

				if (nextKey == '-')
					sp.customerRate -= 0.01;
				
			}

			if (T > nextDraw)
			{
				nextDraw += sp.drawDelta;
				draw();
			}
		}
	}

	public static void main(String[] args)
	{
        if (args.length != 1)
        {
            System.out.println("Usage: java SimParameters [.ssconf file]\nExample: java SimParameters 10checkoutLines.ssconf");
            return;
        }

        SimParameters sp = new SimParameters(args[0]);
		ShoppingSim ss = new ShoppingSim(sp);
		ss.mainLoop();
	}

}