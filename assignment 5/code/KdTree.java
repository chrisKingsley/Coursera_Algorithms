import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class KdTree {
	
	private Node root;
	
	// construct an empty set of points
	public KdTree() {}

	private class Node  {
		Point2D key; // key
		Node left, right; // subtrees
		int N; // # nodes in this subtree
		
		// parent to this node
		Node(Point2D p, int N)
		{
			this.key = p;
			this.N = N;
		}
	}
	
	// is the set empty?
	public boolean isEmpty() {
		return root.N == 0;
	}
	// number of points in the set
	public int size() {
		return size(root);
	}
	
	public int size(Node x) {
		if (x == null)
			return 0;
		return x.N;
	}
	
	
	// add the point p to the set (if it is not already in the set)
	public void insert(Point2D p) {
		// Search for key. Update value if found; grow table if new.
		root = insert(root, p, 1);
	}
	
	private int comparePoints(Point2D h, Point2D p, int depth) {
		int cmpX = Double.compare(h.x(), p.x()),
				cmpY = Double.compare(h.y(), p.y());
		if (depth % 2 == 1)
			return cmpX != 0.0 ? cmpX : cmpY;
		else
			return cmpY != 0.0 ? cmpY : cmpX;
	}
	
	private Node insert(Node h, Point2D p, int depth) {
		// Do standard insert, with red link to parent.
		if (h == null)
			return new Node(p, 1);
		
		int cmp = comparePoints(h.key, p, depth);
		//System.out.printf("depth:%d h.x:%f h.y:%f p.x:%f p.y:%f cmp:%d\n", depth, h.key.x(), h.key.y(), p.x(), p.y(), cmp);
		if (cmp > 0)
			h.left = insert(h.left, p, depth + 1);
		else if (cmp < 0)
			h.right = insert(h.right, p, depth + 1);
		
		h.N = size(h.left) + size(h.right) + 1;
		
		return h;
	}

	// does the set contain the point p?
	public boolean contains(Point2D p) {
		return true;
	}

	// draw all of the points to standard draw
	public void draw() {
		draw(root, 1, 0.0, 0.0, 1.0, 1.0);
	}
	public void draw(Node h, int depth, double minX, double minY, double maxX, double maxY) {
		if (h != null) {
//			System.out.printf("depth:%d point:%s minX:%f minY:%f maxX:%f maxY:%f\n", depth,
//					h.key.toString(), minX, minY, maxX, maxY);
			// draw lines
			StdDraw.setPenRadius(.01);
			if (depth % 2 == 1) {
				StdDraw.setPenColor(StdDraw.RED);
				StdDraw.line(h.key.x(), minY, h.key.x(), maxY);
				draw(h.left, depth + 1, minX, minY, h.key.x(), maxY);
				draw(h.right, depth + 1, h.key.x(), minY, maxX, maxY);
			} else {
				StdDraw.setPenColor(StdDraw.BLUE);
				StdDraw.line(minX, h.key.y(), maxX, h.key.y());
				draw(h.left, depth + 1, minX, minY, maxX, h.key.y());
				draw(h.right, depth + 1, minX, h.key.y(), maxX, maxY);
			}
			
			// plot points
			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.setPenRadius(.02);
	        h.key.draw();
		}
	}

	private void printTree() { printTree(root, 1); }
	private void printTree(Node h, int depth) {
		if (h == null)
			System.out.printf("depth:%d h:%s\n", depth, h);
		else {
			System.out.printf("depth:%d x:%f y:%f\n", depth, h.key.x(), h.key.y());
			printTree(h.left, depth + 1);
			printTree(h.right, depth + 1);
		}
	}
	
	// all points in the set that are inside the rectangle
	public Iterable<Point2D> range(RectHV rect) {
		ArrayList<Point2D> insidePoints = new ArrayList<Point2D>();
		
//		for (Point2D storedPoint : tree) {
//			if (rect.contains(storedPoint)) {
//				insidePoints.add(storedPoint);
//			}
//		}
		
		return insidePoints;
	}

	// a nearest neighbor in the set to p; null if set is empty
	public Point2D nearest(Point2D p) {
		Point2D closestPoint = null;
		
//		for (Point2D storedPoint : tree) {
//			double dist = p.distanceTo(storedPoint);
//			if (dist < closestDist) {
//				closestPoint = storedPoint;
//				closestDist = dist;
//			}
//		}
		
		return closestPoint;
	}
	
	/**
	 * Test the nearest neighbor functionality of the PointSET class
	 */
	private void testNearestNeighbor() {
		while(true) {
			// the location (x, y) of the mouse
            double x = StdDraw.mouseX();
            double y = StdDraw.mouseY();
            Point2D query = new Point2D(x, y);

            // draw all of the points
            StdDraw.clear();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(.01);
            draw();

            // draw in red the nearest neighbor according to the brute-force algorithm
            StdDraw.setPenRadius(.03);
            StdDraw.setPenColor(StdDraw.RED);
            nearest(query).draw();
            StdDraw.setPenRadius(.02);
            
            StdDraw.show(40);
		}
	}
	
	
	public static void main(String[] args) {
		KdTree tree2d = new KdTree();
		
		try {
			BufferedReader infile = new BufferedReader(new FileReader(args[0]));
			while(infile.ready()) {
				String[] tokens = infile.readLine().trim().split(" ");
				Point2D p = new Point2D(Double.parseDouble(tokens[0]), Double.parseDouble(tokens[1]));
				tree2d.insert(p);
			}
			infile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// draw the points
		//tree2d.testNearestNeighbor();
		//tree2d.testRangeSearch();
		tree2d.printTree();
		tree2d.draw();
	}
}