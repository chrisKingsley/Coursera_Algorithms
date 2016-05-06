import static org.junit.Assert.*;

import org.junit.Test;


public class DequeTest {

	@Test
	public void testIsEmpty() {
		Deque<Integer> deque = new Deque<Integer>();
		assertTrue("Deque empty at start", deque.isEmpty());
		
		for(int i=0;i<20;i++)
			deque.addFirst(i+1);
		assertFalse("Deque not empty after adding items", deque.isEmpty());
		
		for(int i=0;i<20;i++)
			deque.removeFirst();
		assertTrue("Deque empty after removing all items", deque.isEmpty());
		
		deque.addLast(10);
		assertFalse("Deque not empty after adding single item", deque.isEmpty());
	}

	@Test
	public void testSize() {
		Deque<Double> deque = new Deque<Double>();
		
		for(double d=0.0;d<10.0;d+=0.1) {
			deque.addFirst(d);
			deque.addLast(20.0-d);
		}
		assertEquals(202, deque.size());
		
		for(int i=0;i<20;i++) {
			deque.removeFirst();
			deque.removeLast();
		}
		assertEquals(162, deque.size());
	}

	@Test
	public void testAddFirst() {
		Deque<String> deque = new Deque<String>();
		deque.addFirst("Hello World");
		
		assertEquals(1, deque.size());
		assertEquals("Hello World", deque.removeFirst());
		assertEquals(0, deque.size());
		
		deque.addFirst("Hello There");
		assertEquals(1, deque.size());
		assertEquals("Hello There", deque.removeLast());
		assertEquals(0, deque.size());
	}

	@Test
	public void testAddLast() {
		Deque<String> deque = new Deque<String>();
		deque.addLast("Hello World");
		
		assertEquals(1, deque.size());
		assertEquals("Hello World", deque.removeFirst());
		assertEquals(0, deque.size());
		
		deque.addLast("Hello There");
		assertEquals(1, deque.size());
		assertEquals("Hello There", deque.removeLast());
		assertEquals(0, deque.size());
	}

	@Test
	public void testRemoveFirst() {
		Deque<Integer> deque = new Deque<Integer>();
		
		for(int i=0;i<50;i++)
			deque.addLast(i);
		
		int count = 0;
		while(!deque.isEmpty()) {
			int removedInt = deque.removeFirst();
			//System.out.println(count + ") removeFirst:" + removedInt);
			assertEquals(count, removedInt);
			count++;
		}
	}

	@Test
	public void testRemoveLast() {
		Deque<Integer> deque = new Deque<Integer>();
		
		for(int i=50;i>=0;i--)
			deque.addLast(i);
		
		int count = 0;
		while(!deque.isEmpty()) {
			int removedInt = deque.removeLast();
			//System.out.println(count + ") removeLast:" + removedInt);
			assertEquals(count, removedInt);
			count++;
		}
	}

	@Test
	public void testIterator() {
		Deque<Character> deque = new Deque<Character>();
		for(char c='A';c<='Z';c++) {
			//System.out.println("adding char:" + c);
			deque.addLast(c);
		}
		
		char check = 'A';
		for(char c: deque) {
			//System.out.println(check + ") iterator char:" + c);
			assertEquals(check++, c);
			
			char check2 = 'A';
			for(char c2: deque) {
				//System.out.println(check2 + ") iterator char2:" + c2);
				assertEquals(check2++, c2);
			}
				
		}
	}

}
