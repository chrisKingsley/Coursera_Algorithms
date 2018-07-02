import java.util.ArrayList;

// class for testing red/black vs. KD tree
public class TreeTests {
	private static final int NUM_SEARCHES = 10000;
	

	// check that red/black and kdTree give same nearest neighbor search results
	public static void nearestNeighborAccuracy(String pointFile) {
		PointSET brute = new PointSET();
		KdTree kdTree = new KdTree();
		
		// initialize the two data structures with point from standard input
		In in = new In(pointFile);
		while (!in.isEmpty()) {
			double x = in.readDouble();
			double y = in.readDouble();
			Point2D p = new Point2D(x, y);
			
            kdTree.insert(p);
            brute.insert(p);
		}
		
		boolean differenceFound = false;
		for (int i = 0; i < NUM_SEARCHES; i++) {
			double x = Math.random();
            double y = Math.random();
            Point2D p = new Point2D(x, y);
            
            Point2D bruteNearest = brute.nearest(p);
            Point2D kdTreeNearest = kdTree.nearest(p);
            
            if (!bruteNearest.equals(kdTreeNearest)) {
            	System.out.println("p:" + p.toString() + " bruteNearest:" + bruteNearest.toString() +
            			" kdTreeNearest:" + kdTreeNearest.toString());
            	differenceFound = true;
            }
		}
		if (!differenceFound) {
        	System.out.println("No brTree/kdTree discrepancies found for NN search");
        }
	}
	
	
	// run time analysis for nearest neighbor search in red/black vs. kd tree
	public static void nearestNeighborTimeComparison() {
		System.out.println("numPoints\tbrute_time\tkdTree_time");
		
		for (int numPoints = 10000; numPoints <= 1000000; numPoints += 10000) {
			PointSET brute = new PointSET();
			KdTree kdTree = new KdTree();
			
			// add points
			for (int i=0; i < numPoints; i++) {
				double x = Math.random();
	            double y = Math.random();
	            Point2D p = new Point2D(x, y);
	            kdTree.insert(p);
	            brute.insert(p);
			}
			
			// timer for nearest neighbor search
			long startTime = System.currentTimeMillis();
			for (int i = 0; i < NUM_SEARCHES; i++) {
				double x = Math.random();
	            double y = Math.random();
	            Point2D p = new Point2D(x, y);
	            brute.nearest(p);
			}
			double bruteTime = (System.currentTimeMillis() - startTime)/1000.0;
			
			startTime = System.currentTimeMillis();
			for (int i = 0; i < NUM_SEARCHES; i++) {
				double x = Math.random();
	            double y = Math.random();
	            Point2D p = new Point2D(x, y);
	            kdTree.nearest(p);
			}
			double kdTime = (System.currentTimeMillis() - startTime)/1000.0;
			System.out.printf("%d\t%f\t%f\n", numPoints, bruteTime, kdTime);
		}
	}
	
	
	// run time analysis for nearest neighbor search in kd tree
	public static void nearestNeighborTimer() {
		System.out.println("numPoints\tkdTree_time");
		
		for (int numPoints = 10000; numPoints <= 1000000; numPoints += 10000) {
			KdTree kdTree = new KdTree();
			
			// add points
			for (int i=0; i < numPoints; i++) {
				double x = Math.random();
	            double y = Math.random();
	            Point2D p = new Point2D(x, y);
	            kdTree.insert(p);
			}
			
			// get nearest neighbor from kd tree
			long startTime = System.currentTimeMillis();
			for (int i = 0; i < NUM_SEARCHES; i++) {
				double x = Math.random();
	            double y = Math.random();
	            Point2D p = new Point2D(x, y);
	            kdTree.nearest(p);
			}
			System.out.printf("%d\t%f\n", numPoints, (System.currentTimeMillis() - startTime)/1000.0);
		}
	}
	
	
	// check that red/black and kdTree give same range search results
	public static void rangeSearchAccuracy(String pointFile) {
		PointSET brute = new PointSET();
		KdTree kdTree = new KdTree();
		
		// initialize the two data structures with point from standard input
		In in = new In(pointFile);
		while (!in.isEmpty()) {
			double x = in.readDouble();
			double y = in.readDouble();
			Point2D p = new Point2D(x, y);
			
            kdTree.insert(p);
            brute.insert(p);
		}
		
		boolean differenceFound = false;
		for (int i = 0; i < NUM_SEARCHES; i++) {
			double x1 = Math.random(), x2 = Math.random();
            double y1 = Math.random(), y2 = Math.random();
            RectHV rect = new RectHV(Math.min(x1,x2), Math.min(y1,y2), Math.max(x1,x2), Math.max(y1,y2));
            
            ArrayList<Point2D> bruteSet = (ArrayList<Point2D>)brute.range(rect);
            ArrayList<Point2D> kdTreeSet = (ArrayList<Point2D>)kdTree.range(rect);
            
            if(bruteSet.size() != kdTreeSet.size()) {
            	differenceFound = true;
        		System.out.println("Difference found");
            } else {
	            for (Point2D p : bruteSet) {
	            	if (!kdTreeSet.contains(p)) {
	            		differenceFound = true;
	            		System.out.println("Difference found");
	            	}
	            }
            }
		}
		if (!differenceFound) {
        	System.out.println("No brTree/kdTree discrepancies found for range search");
        }
	}
	
	
	// run time analysis for range search in red/black vs. kd tree
	public static void rangeSearchTimeComparison() {
		System.out.println("numPoints\tbrute_time\tkdTree_time");
		
		for (int numPoints = 100; numPoints <= 10000; numPoints += 100) {
			PointSET brute = new PointSET();
			KdTree kdTree = new KdTree();
			
			// add points
			for (int i=0; i < numPoints; i++) {
				double x = Math.random();
	            double y = Math.random();
	            Point2D p = new Point2D(x, y);
	            kdTree.insert(p);
	            brute.insert(p);
			}
			
			// timer for range search
			long startTime = System.currentTimeMillis();
			for (int i = 0; i < NUM_SEARCHES; i++) {
				double x1 = Math.random(), x2 = Math.random();
	            double y1 = Math.random(), y2 = Math.random();
	            RectHV rect = new RectHV(Math.min(x1,x2), Math.min(y1,y2), Math.max(x1,x2), Math.max(y1,y2));
	            
	            Iterable<Point2D> bruteSet = brute.range(rect);
			}
			double bruteTime = (System.currentTimeMillis() - startTime)/1000.0;
			
			startTime = System.currentTimeMillis();
			for (int i = 0; i < NUM_SEARCHES; i++) {
				double x1 = Math.random(), x2 = Math.random();
	            double y1 = Math.random(), y2 = Math.random();
	            RectHV rect = new RectHV(Math.min(x1,x2), Math.min(y1,y2), Math.max(x1,x2), Math.max(y1,y2));
	            
	            Iterable<Point2D> kdTreeSet = kdTree.range(rect);
			}
			double kdTime = (System.currentTimeMillis() - startTime)/1000.0;
			System.out.printf("%d\t%f\t%f\n", numPoints, bruteTime, kdTime);
		}
	}
	
	
	// run time analysis for range search in kd tree
	public static void rangeSearchTimer() {
		System.out.println("numPoints\tkdTree_time");
		
		for (int numPoints = 1000; numPoints <= 100000; numPoints += 1000) {
			KdTree kdTree = new KdTree();
			
			// add points
			for (int i=0; i < numPoints; i++) {
				double x = Math.random();
	            double y = Math.random();
	            Point2D p = new Point2D(x, y);
	            kdTree.insert(p);
			}
			
			long startTime = System.currentTimeMillis();
			for (int i = 0; i < NUM_SEARCHES; i++) {
				double x1 = Math.random(), x2 = Math.random();
	            double y1 = Math.random(), y2 = Math.random();
	            RectHV rect = new RectHV(Math.min(x1,x2), Math.min(y1,y2), Math.max(x1,x2), Math.max(y1,y2));
	            
	            Iterable<Point2D> kdTreeSet = kdTree.range(rect);
			}
			System.out.printf("%d\t%f\n", numPoints, (System.currentTimeMillis() - startTime)/1000.0);
		}
	}
	
	public static void main(String[] args) {
		rangeSearchTimer();
	}

}
