import java.util.ArrayList;

// class representing a Kd tree
public class KdTree {

	private Node root;
	private double closestDist;

	// private class representing a node in the tree
	private class Node  {
		Point2D key; // key
		Node left, right; // subtrees
		int N; // # nodes in this subtree
		RectHV rect;
		
		// parent to this node
		Node(Point2D p, RectHV rect, int N)
		{
			this.key = p;
			this.N = N;
			this.rect = rect;
		}
		
		public String toString() {
			return String.format("key:%s rect:%s N:%d", key.toString(), rect.toString(), N);
		}
	}
	
	// is the set empty?
	public boolean isEmpty() {
		return size(root) == 0;
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
	
	
	// compare points, with depth in the tree determining which dimension to use
	private int comparePoints(Point2D h, Point2D p, int depth) {
		int cmpX = Double.compare(h.x(), p.x()),
				cmpY = Double.compare(h.y(), p.y());
		if (depth % 2 == 1)
			return cmpX != 0.0 ? cmpX : cmpY;
		else
			return cmpY != 0.0 ? cmpY : cmpX;
	}
	
	
	// add the point p to the set (if it is not already in the set)
	public void insert(Point2D p) {
		root = insert(root, p, 1);
	}
	private Node insert(Node h, Point2D p, int depth) {
		// Insert new node
		if (h == null)
			return new Node(p, new RectHV(p.x(),p.y(),p.x(),p.y()), depth);
		
		// follow left and right nodes
		int cmp = comparePoints(h.key, p, depth);
		if (cmp > 0)
			h.left = insert(h.left, p, depth + 1);
		else if (cmp < 0)
			h.right = insert(h.right, p, depth + 1);
		
		// update number and rectangle for the node
		h.N = size(h.left) + size(h.right) + 1;
		h.rect = new RectHV(Math.min(h.rect.xmin(), p.x()), Math.min(h.rect.ymin(), p.y()),
				            Math.max(h.rect.xmax(), p.x()), Math.max(h.rect.ymax(), p.y()));
		
		return h;
	}

	
	// does the set contain the point p?
	public boolean contains(Point2D p) {
		return contains(root, p, 1);
	}
	private boolean contains(Node h, Point2D p, int depth) {
		if(h == null)
			return false;
		else {		
			int cmp = comparePoints(h.key, p, depth);
			if(cmp == 0)
				return true;
			else if(cmp < 0)
				return contains(h.left, p, depth + 1);
			else
				return contains(h.right, p, depth + 1);	
		}
	}

	
	// draw all of the points to standard draw
	public void draw() {
		draw(root, 1, 0.0, 0.0, 1.0, 1.0);
	}
	public void draw(Node h, int depth, double minX, double minY, double maxX, double maxY) {
		if (h != null) {
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
	
	// print nodes of tree to stdout
	public void printTree() { 
		printTree(root, 1);
	}
	private void printTree(Node h, int depth) {
		if (h != null) {
			System.out.printf("%s depth:%d\n", h.toString(), depth);
			printTree(h.left, depth + 1);
			printTree(h.right, depth + 1);
		}
	}
	
	
	// all points in the set that are inside the rectangle
	public Iterable<Point2D> range(RectHV rect) {
		ArrayList<Point2D> insidePoints = new ArrayList<Point2D>();
		range(root, rect, insidePoints);
		
		return insidePoints;
	}
	private void range(Node h, RectHV rect, ArrayList<Point2D> insidePoints) {
		if(h != null && h.rect.intersects(rect)) {
			if(rect.contains(h.key))
				insidePoints.add(h.key);
			
			range(h.left, rect, insidePoints);
			range(h.right, rect, insidePoints);
		}
	}
	
	
	// a nearest neighbor in the set to p; null if set is empty
	public Point2D nearest(Point2D p) {
		closestDist = Double.MAX_VALUE;
		return nearest(root, p, null, 1);
	}
	private Point2D nearest(Node h, Point2D p, Point2D closestPoint, int depth) {
		if (h != null && h.rect.distanceTo(p) < closestDist) {
			
			// update closestPoint if current point is closer
			if(h.key.distanceTo(p) < closestDist) {
				closestDist = h.key.distanceTo(p);
				closestPoint = h.key;
			}
			
			int cmp = comparePoints(h.key, p, depth);
			if (cmp > 0) {
				closestPoint = nearest(h.left, p, closestPoint, depth + 1);
				closestPoint = nearest(h.right, p, closestPoint, depth + 1);
			} else if (cmp < 0) {
				closestPoint = nearest(h.right, p, closestPoint, depth + 1);
				closestPoint = nearest(h.left, p, closestPoint, depth + 1);
			}
		}
		
		return closestPoint;
	}
	
	
	public static void main(String[] args) {
		KdTree kdtree = new KdTree();

        // initialize the two data structures with point from standard input
//		In in = new In("C:/Users/ckingsley/Desktop/input100.txt");
//		while (!in.isEmpty()) {
//            double x = in.readDouble();
//            double y = in.readDouble();
//            Point2D p = new Point2D(x, y);
//            kdtree.insert(p);
//        }
		
//		 draw the points
//		kdtree.testNearestNeighbor();
//		kdtree.testRangeSearch();
//        kdtree.printTree();
//		kdtree.draw();
		
        
		//RangeSearchVisualizer.main(args);
		NearestNeighborVisualizer.main(args);
//		KdTreeVisualizer.main(args);
//		TreeTests.nearestNeighborTimer();
	}
}

