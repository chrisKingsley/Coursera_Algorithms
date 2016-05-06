import java.util.Iterator;

/**
 * Implementation of a double ended queue (deque) using a doubly linked list.
 * Implemented as a generic to accommodate deques of multiple type.
 * Implements Iterable.
 * @author ckingsley
 *
 * @param <Item>
 */
public class Deque<Item> implements Iterable<Item> {
	
	private Node first, last;
	private int count;
	
	
	/**
	 * construct an empty deque
	 */
	public Deque() {
		first = last = null;
		count = 0;
	}
	
	
	/**
	 * is the deque empty?
	 * @return
	 */
	public boolean isEmpty() {
		return count==0;
	}
	
	
	/**
	 * return the number of nodes in the deque
	 * @return
	 */
	public int size() {
		return count;
	}
	
	
	/**
	 * insert the item at the front
	 * @param item
	 */
	public void addFirst(Item item) {
		if(item==null)
			throw new java.lang.NullPointerException("attempted to add null item to beginning of deque");
		
		Node node = new Node();
		node.item = item;
		node.next = first;
		if(first!=null)
			first.prev = node;
		first = node;
		if(count==0)
			last=node;
		
		count++;
	}
	
	
	/**
	 * insert the item at the end
	 * @param item
	 */
	public void addLast(Item item) {
		if(item==null)
			throw new java.lang.NullPointerException("attempted to add null item to end of deque");
		
		Node node = new Node();
		node.item = item;
		if(last!=null)
			last.next = node;
		node.prev = last;
		last = node;
		if(count==0)
			first = node;
		
		count++;
	}
	
	
	/**
	 * delete and return the item at the front
	 * @return
	 */
	public Item removeFirst() {
		if(first==null)
			throw new java.util.NoSuchElementException("Can't remove from beginning of empty deque");
		
		Item item = first.item;
		first = first.next;
		if(first!=null)
			first.prev = null;
		count--;
		
		if(isEmpty()) last=null;
		
		return item;
	}
	
	
	/**
	 * delete and return the item at the end
	 * @return
	 */
	public Item removeLast() {
		if(last==null)
			throw new java.util.NoSuchElementException("Can't remove from end of empty deque");
		
		Item item = last.item;
		last = last.prev;
		if(last!=null)
			last.next = null;
		count--;
		
		if(isEmpty()) first=null;
		
		return item;
	}
	
	
	/**
	 * return an iterator over items in order from front to end
	 * @return
	 */
	public Iterator<Item> iterator() {
		return new DequeIterator();
	}
	
	
	/**
	 * Iterator class so the deque can implement Iterator<Item>
	 * @author ckingsley
	 *
	 */
	private class DequeIterator implements Iterator<Item> {
		private Node iterNode;
		
		private DequeIterator() {
			iterNode = first;
		}
		public boolean hasNext() {
			return iterNode!=null;
		}
		public Item next() {
			if(!hasNext())
				throw new java.util.NoSuchElementException("No additional elements to iterate over");
			
			Item item = iterNode.item;
			iterNode = iterNode.next;
			return item;
		}
		/**
		 * not implemented
		 */
		public void remove() {
			throw new java.lang.UnsupportedOperationException("removal of elements in iterator not allowed");
		}
	}
	
	
	/**
	 * Class representing a node in the doubly linked list
	 * @author ckingsley
	 *
	 */
	private class Node {
		Item item;
		Node prev, next;
		
		Node() {
			item = null;
			prev = next = null;
		}
	}

}
