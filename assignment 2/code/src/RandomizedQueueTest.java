import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;


public class RandomizedQueueTest {

	@Test
	public void testIsEmpty() {
		RandomizedQueue<Integer> ranQueue = new RandomizedQueue<Integer>();
		
		for(int i=0;i<20;i++) {
			for(int j=1;j<=100;j++)
				ranQueue.enqueue(StdRandom.uniform(j));
			assertFalse(ranQueue.isEmpty());
			for(int j=0;j<100;j++)
				ranQueue.dequeue();
			assertTrue(ranQueue.isEmpty());
		}
	}

	@Test
	public void testSize() {
		RandomizedQueue<Character> ranQueue = new RandomizedQueue<Character>();
		
		int count=1;
		for(char c='a';c<='z';c++) {
			ranQueue.enqueue(c);
			assertEquals(count++, ranQueue.size());
		}
	}

	@Test
	public void testEnqueue() {
		RandomizedQueue<Integer> ranQueue = new RandomizedQueue<Integer>();
		int[] hits = new int[100];
		
		for(int i=0;i<100;i++)
			ranQueue.enqueue(i);
		for(int i=0;i<100;i++)
			hits[ranQueue.dequeue()] = 1;
		for(int i=0;i<100;i++)
			assertEquals(1, hits[i]);
		
	}

	@Test
	public void testDequeue() {
		RandomizedQueue<String> ranQueue = new RandomizedQueue<String>();
		String [] tokens = "AA BB BB BB BB BB CC CC".split(" ");
		for(String token : tokens)
			ranQueue.enqueue(token);
		
		for(int i=0;i<5;i++)
			ranQueue.dequeue();
		
		assertEquals(3, ranQueue.size());
	}

	@Test
	public void testSample() {
		RandomizedQueue<Integer> ranQueue = new RandomizedQueue<Integer>();
		
		for(int i=0;i<100;i++)
			ranQueue.enqueue(i);
		assertEquals(100, ranQueue.size());
		
		for(int i=0;i<1000;i++) {
			int sample = ranQueue.sample();
			assertTrue(sample>=0 && sample<100);
		}
		assertEquals(100, ranQueue.size());
	}

	@Test
	public void testIterator() {
		RandomizedQueue<Character> ranQueue = new RandomizedQueue<Character>();
		
		for(char c='a';c<='z';c++) 
			ranQueue.enqueue(c);
			
		int numDifferent = 0;
		Iterator<Character> iter1 = ranQueue.iterator();
		Iterator<Character> iter2 = ranQueue.iterator();
		while(iter1.hasNext()) {
			char c1=iter1.next(), c2=iter2.next();
			if(c1!=c2)
				numDifferent++;
		}
		assertTrue(numDifferent>0);
		
	}

}
