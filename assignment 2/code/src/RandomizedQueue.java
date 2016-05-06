import java.util.Iterator;


public class RandomizedQueue<Item> implements Iterable<Item> {
	
	private static final int INTITIAL_SIZE=100;
	private int count;
	private Item[] items;
	
	/**
	 * construct an empty randomized queue
	 */
	public RandomizedQueue() {
		count = 0;
		items = (Item[]) new Object[INTITIAL_SIZE];
	}
	
	/**
	 * construct an empty randomized queue of passed initial size
	 */
	public RandomizedQueue(int intitialSize) {
		count = 0;
		items = (Item[]) new Object[intitialSize];
	}
	
	/**
	 * is the queue empty?
	 * @return
	 */
	public boolean isEmpty() {
		return count==0;
	}
	
	/**
	 * return the number of items on the queue
	 * @return
	 */
	public int size() {
		return count;
	}
	
	/**
	 * Add the item to the random queue.  Put the new item in a random position in the queue,
	 * and put the item it displaces at the end of the queue
	 * @param item
	 */
	public void enqueue(Item item) {
		if(item==null)
			throw new java.lang.NullPointerException("Cannot add null pointer to queue");
		
		if(count >= items.length)
			resize(2*items.length);
		
		items[count] = item;
		if(count>0) {
			int randomPos = StdRandom.uniform(count+1);
			Item temp = items[randomPos];
			items[randomPos] = items[count];
			items[count] = temp;
		}
		count++;
	}
	
	/**
	 * delete and return a random item
	 * @return
	 */
	public Item dequeue() {
		if(isEmpty())
			throw new java.util.NoSuchElementException("cannot dequeue from empty queue");
		
		Item temp = items[--count];
		items[count] = null;
		if(count <= items.length/4 && items.length>0)
			resize(items.length/2);
		return temp;
	}
	
	/**
	 * return (but do not delete) a random item
	 * @return
	 */
	public Item sample() {
		if(isEmpty())
			throw new java.util.NoSuchElementException("cannot sample from empty queue");
		
		return items[ StdRandom.uniform(count) ];
	}
	
	
	/**
	 * resizes array and copies over array contents
	 * @param max
	 */
	private void resize(int max)
    {
		// Move stack to a new array of size max.
		Item[] temp = (Item[]) new Object[max];
		for (int i = 0; i < count; i++)
			temp[i] = items[i];
		items = temp;
    }
	
	
//	/**
//	 * prints array contents to stdout
//	 */
//	public void printArray() {
//		for(int i=0;i<count;i++)
//			System.out.println(i + ":" + items[i]);
//	}
	
	
	/**
	 * return an independent iterator over items in random order
	 * @return
	 */
	public Iterator<Item> iterator() {
		return new RandomizedQueueIterator();
	}
	
	/**
	 * Class instantiating the iterator
	 * @author ckingsley
	 *
	 */
	private class RandomizedQueueIterator implements Iterator<Item> {
		private int[] indices = new int[count];
		private int index;
		
		RandomizedQueueIterator() {
			index = 0;
			
			for(int i=0;i<count;i++)
				indices[i] = i;
			StdRandom.shuffle(indices);
		}
		
		public boolean hasNext() {
			return index<count;
		}
		
		public Item next() {
			if(!hasNext())
				throw new java.util.NoSuchElementException("Cannot iterate past end of queue");
				
			return items[ indices[index++] ];
		}
		
		public void remove() {
			throw new java.lang.UnsupportedOperationException("removal of elements in iterator of queue not allowed");
		}
	}


}
