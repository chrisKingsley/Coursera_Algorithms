import java.util.ArrayList;

public class KdTreePoints {
	
	private Node root;
	private double closestDist;

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
		// Search for key. Update value if found; grow table if new.
		root = insert(root, p, 1);
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
	
	// print nodes of tree to stdout
	public void printTree() { 
		printTree(root, 1);
	}
	private void printTree(Node h, int depth) {
		if (h != null) {
			System.out.printf("depth:%d x:%f y:%f\n", depth, h.key.x(), h.key.y());
			printTree(h.left, depth + 1);
			printTree(h.right, depth + 1);
		}
	}
	
	// all points in the set that are inside the rectangle
	public Iterable<Point2D> range(RectHV rect) {
		ArrayList<Point2D> insidePoints = new ArrayList<Point2D>();
		range(root, rect, insidePoints, 1);
		
		return insidePoints;
	}
	private void range(Node h, RectHV rect, ArrayList<Point2D> insidePoints, int depth) {
		if(h != null) {
			if(rect.contains(h.key))
				insidePoints.add(h.key);
			if(depth % 2 == 1) {
				if(rect.xmin() <= h.key.x())
					range(h.left, rect, insidePoints, depth + 1);
				if(rect.xmax() >= h.key.x())
					range(h.right, rect, insidePoints, depth + 1);
			} else {
				if(rect.ymin() <= h.key.y())
					range(h.left, rect, insidePoints, depth + 1);
				if(rect.ymax() >= h.key.y())
					range(h.right, rect, insidePoints, depth + 1);
			}
		}
	}
	
	private double distToNode(Point2D p, double minX, double minY, double maxX, double maxY) {
		double dx = 0.0, dy = 0.0;
        if      (p.x() < minX) dx = p.x() - minX;
        else if (p.x() > maxX) dx = p.x() - maxX;
        if      (p.y() < minY) dy = p.y() - minY;
        else if (p.y() > maxY) dy = p.y() - maxY;
        return dx*dx + dy*dy;
	}
	
	// a nearest neighbor in the set to p; null if set is empty
	public Point2D nearest(Point2D p) {
		closestDist = Double.MAX_VALUE;
		return nearest(root, p, null, 1, 0.0, 0.0, 1.0, 1.0);
	}
	private Point2D nearest(Node h, Point2D p, Point2D closestPoint, int depth,
			double minX, double minY, double maxX, double maxY) {
		if (h != null) {
			// exit if closest distance is closer than point p to node
			if (closestDist < distToNode(p, minX, minY, maxX, maxY))
				return closestPoint;
			
			// update closestPoint if current point is closer
			if(h.key.distanceTo(p) < closestDist) {
				closestDist = h.key.distanceTo(p);
				closestPoint = h.key;
			}
			
			
			int cmp = comparePoints(h.key, p, depth);
//			System.out.println("cmp:" + cmp + " current:" + h.key.toString() + " closest:" + closestPoint.toString() +
//					" bestDist:" + closestDist + " depth:" + depth + " distToRect:" + distToRect);
			
			// traverse tree to find closest point, pruning the rectangle defined by each node
			if(cmp > 0) {
				if (depth % 2 == 1) {
					closestPoint = nearest(h.left, p, closestPoint, depth + 1, minX, minY, h.key.x(), maxY);
					closestPoint = nearest(h.right, p, closestPoint, depth + 1,  h.key.x(), minY, maxX, maxY);
				} else {
					closestPoint = nearest(h.left, p, closestPoint, depth + 1, minX, minY, maxX, h.key.y());
					closestPoint = nearest(h.right, p, closestPoint, depth + 1,  minX, h.key.y(), maxX, maxY);
				}
			} else if(cmp < 0) {
				if (depth % 2 == 1) {
					closestPoint = nearest(h.right, p, closestPoint, depth + 1,  h.key.x(), minY, maxX, maxY);
					closestPoint = nearest(h.left, p, closestPoint, depth + 1, minX, minY,  h.key.x(), maxY);
				} else {
					closestPoint = nearest(h.right, p, closestPoint, depth + 1, minX, h.key.y(), maxX, maxY);
					closestPoint = nearest(h.left, p, closestPoint, depth + 1, minX, minY, maxX, h.key.y());
				}
			}
		}
		
		return closestPoint;
	}
	
	
	public static void main(String[] args) {
		KdTreePoints kdtree = new KdTreePoints();

//        // initialize the two data structures with point from standard input
//		In in = new In(args[0]);
//		while (!in.isEmpty()) {
//            double x = in.readDouble();
//            double y = in.readDouble();
//            Point2D p = new Point2D(x, y);
//            kdtree.insert(p);
//        }
		
		// draw the points
		//kdtree.testNearestNeighbor();
		//kdtree.testRangeSearch();
        //kdtree.printTree();
		//kdtree.draw();
		
        
		//RangeSearchVisualizer.main(args);
		//NearestNeighborVisualizer.main(args);
		KdTreeVisualizer.main(args);
	}
}