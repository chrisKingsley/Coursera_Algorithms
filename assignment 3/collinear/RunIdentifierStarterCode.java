/*************************************************************************
* Name: 
* Login: 
* Precept: 

* Partner Name: 
* Partner Login: 
* Partner Precept: 
*
* It is not necessary to include partners for this challenge.
* Make sure you include your partner on your submission for
* the collinear assignment.
* 
* Compilation:  javac RunIdentifier
* Example Execution: java RunIdentifier [input file]
*					   [input file] is optional
* 
* Prints all runs in an array above some minimum size
* 
* Starter code by: jhug@cs.princeton.edu
*
*************************************************************************/

public class RunIdentifier {

	//prints all runs in a that contain at least minNumPoints

	public static void identifyRuns(int[] a, int minNumPoints)
	{
		//your code here
	}


	// If an input file is provided as a command line argument, the
	// input from that file is used

	// If no input file is provided, then input is taken from StdIn

	public static void main(String[] args)
	{
		int[] intArray;
		int minRunLength = 3;

		if (args.length == 0)
		{
			System.out.print("No input file specified on command line, defaulting to a run length of 3. Collecting input from StdIn instead.\n\nTo specify an input file and run length instead, use 'java RunIdentifier [inputFile] [minRunLength]'\n\nEnter integer values separated by white space: ");

			intArray = StdIn.readInts();
		}
		else
		{
			if (args.length < 2)
				System.out.print("No run length specified on command line, defaulting to a run length of 3.\n\nTo specify a run length instead, use 'java RunIdentifier [inputFile] [minRunLength]'\n\n");
			else
				minRunLength = Integer.parseInt(args[1]);

			intArray = In.readInts(args[0]);
		}

		identifyRuns(intArray, minRunLength);
	}
}