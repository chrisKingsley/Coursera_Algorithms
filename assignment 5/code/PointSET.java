import java.util.ArrayList;
import java.util.TreeSet;

public class PointSET {
	TreeSet<Point2D> tree;
	
	// construct an empty set of points
	public PointSET() {
		tree = new TreeSet<Point2D>();
	}

	// is the set empty?
	public boolean isEmpty() {
		return tree.size() == 0;
	}
	
	// number of points in the set
	public int size() {
		return tree.size();
	}

	// add the point p to the set (if it is not already in the set)
	public void insert(Point2D p) {
		tree.add(p);
	}

	// does the set contain the point p?
	public boolean contains(Point2D p) {
		return true;
	}

	// draw all of the points to standard draw
	public void draw() {
		
	}
	
	
	/**
	 * all points in the set that are inside the rectangle
	 * @param rect RectHV object that defines the rectangle
	 * @return ArrayList of points inside the rectangle
	 */
	public Iterable<Point2D> range(RectHV rect) {
		ArrayList<Point2D> insidePoints = new ArrayList<Point2D>();
		
		for (Point2D storedPoint : tree) {
			if (rect.contains(storedPoint)) {
				insidePoints.add(storedPoint);
			}
		}
		
		return insidePoints;
	}

	/**
	 * a nearest neighbor in the set to p; null if set is empty
	 * @param p
	 * @return
	 */
	public Point2D nearest(Point2D p) {
		Point2D closestPoint = null;
		double closestDist = Double.MAX_VALUE;
		
		for (Point2D storedPoint : tree) {
			double dist = p.distanceTo(storedPoint);
			if (dist < closestDist) {
				closestPoint = storedPoint;
				closestDist = dist;
			}
		}
		
		return closestPoint;
	}
}