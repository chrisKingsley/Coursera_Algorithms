import java.util.ArrayList;

// class representing a Kd tree, using rectangles in the node
public class KdTree {

	private Node root;
	private double closestDist;

	// private class representing a node in the tree
	class Node {
		Point2D key; // key
		Node left, right; // subtrees
		int N; // # nodes in this subtree
		RectHV rect;
		
		// parent to this node
		Node(Point2D p, RectHV rect, int N) {
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
	private int comparePoints(Point2D h, Point2D p, boolean useX) {
		int cmpX = Double.compare(h.x(), p.x()),
				cmpY = Double.compare(h.y(), p.y());
		if (useX)
			return cmpX != 0.0 ? cmpX : cmpY;
		else
			return cmpY != 0.0 ? cmpY : cmpX;
	}
	
	
	// add the point p to the set (if it is not already in the set)
	public void insert(Point2D p) {
		root = insert(root, p, true);
	}
	private Node insert(Node h, Point2D p, boolean useX) {
		// Insert new node if at end of tree
		if (h == null)
			return new Node(p, new RectHV(p.x(),p.y(),p.x(),p.y()), 1);
		
		// recursively follow left and right nodes
		int cmp = comparePoints(h.key, p, useX);
		if (cmp > 0)
			h.left = insert(h.left, p, !useX);
		else if (cmp < 0)
			h.right = insert(h.right, p, !useX);
		
		// update number and rectangle for the node
		h.N = size(h.left) + size(h.right) + 1;
		h.rect = new RectHV(Math.min(h.rect.xmin(), p.x()), Math.min(h.rect.ymin(), p.y()),
				            Math.max(h.rect.xmax(), p.x()), Math.max(h.rect.ymax(), p.y()));
		
		return h;
	}

	
	// does the set contain the point p?
	public boolean contains(Point2D p) {
		return contains(root, p, true);
	}
	private boolean contains(Node h, Point2D p, boolean useX) {
		if(h == null)
			return false;
		else {		
			int cmp = comparePoints(h.key, p, useX);
			if(cmp == 0)
				return true;
			else if(cmp < 0)
				return contains(h.left, p, !useX);
			else
				return contains(h.right, p, !useX);	
		}
	}

	
	// draw all of the points to standard draw
	public void draw() {
		draw(root, true, 0.0, 0.0, 1.0, 1.0);
	}
	public void draw(Node h, boolean useX, double minX, double minY, double maxX, double maxY) {
		if (h != null) {
			// draw lines
			StdDraw.setPenRadius(.01);
			if (useX) {
				StdDraw.setPenColor(StdDraw.RED);
				StdDraw.line(h.key.x(), minY, h.key.x(), maxY);
				draw(h.left, !useX, minX, minY, h.key.x(), maxY);
				draw(h.right, !useX, h.key.x(), minY, maxX, maxY);
			} else {
				StdDraw.setPenColor(StdDraw.BLUE);
				StdDraw.line(minX, h.key.y(), maxX, h.key.y());
				draw(h.left, !useX, minX, minY, maxX, h.key.y());
				draw(h.right, !useX, minX, h.key.y(), maxX, maxY);
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
		return nearest(root, p, null, true);
	}
	private Point2D nearest(Node h, Point2D p, Point2D closestPoint, boolean useX) {
		if (h != null && h.rect.distanceTo(p) < closestDist) {
			
			// update closestPoint if current point is closer
			if(h.key.distanceTo(p) < closestDist) {
				closestDist = h.key.distanceTo(p);
				closestPoint = h.key;
			}
			
			// recursively search the subtrees
			int cmp = comparePoints(h.key, p, useX);
			if (cmp > 0) {
				closestPoint = nearest(h.left, p, closestPoint, !useX);
				closestPoint = nearest(h.right, p, closestPoint, !useX);
			} else if (cmp < 0) {
				closestPoint = nearest(h.right, p, closestPoint, !useX);
				closestPoint = nearest(h.left, p, closestPoint, !useX);
			}
		}
		
		return closestPoint;
	}
}

