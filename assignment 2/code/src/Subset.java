
public class Subset {

	/**
	 * Client to test RandomizedQueue
	 * @param args
	 */
	
	public static void main(String[] args) {
		if(args.length<1)
			throw new java.lang.IllegalArgumentException("The number of items to print must be an argument");
		
		int k=Integer.parseInt(args[0]);
		
		RandomizedQueue<String> ranQueue = new RandomizedQueue<String>();
		String [] tokens = "AA BB BB BB BB BB CC CC".split(" ");
		for(String token : tokens)
			ranQueue.enqueue(token);
		System.out.println("queue size:" + ranQueue.size());
		
		for(String item:ranQueue)
			System.out.println("iter1:" + item);
		
		for(int i=0;i<k;i++)
			System.out.println("dequeue:" + ranQueue.dequeue());
		System.out.println("queue size:" + ranQueue.size());
		
		for(String item:ranQueue)
			System.out.println("iter2:" + item);
		
//		int arraySize = 50, numPerms=100000;
//		int[] arrayCounts = new int[arraySize];
//		for(int p=0;p<numPerms;p++) {
//			RandomizedQueue<Integer> ranQueue = new RandomizedQueue<Integer>();
//			
//			for(int i=0;i<arraySize;i++) {
//				ranQueue.enqueue(i);
//			}
//			//System.out.println("queue size:" + ranQueue.size());
//			
//	//		System.out.println("Iterating:");
//	//		for(int i:ranQueue) {
//	//			System.out.println("iter:" + i); 
//	//		}
//			
//			
//	//		System.out.println("Printing " + k + " items");
//	//		for(int i=0;i<k;i++) {
//	//			int item = ranQueue.dequeue();
//	//			System.out.println(item);
//	//		}
//			
//			for(int i=0;i<arraySize;i++) {
//				int item = ranQueue.dequeue();
//				arrayCounts[item] += i;
//			}
//		
//		}
//		
//		for(int i=0;i<arraySize;i++) {
//			System.out.println("item:" + i + " counts:" + arrayCounts[i]);
//		}
		
	}

}
