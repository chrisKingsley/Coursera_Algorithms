
import java.util.Comparator;



public class Point implements Comparable<Point>  {
	
	// compare points by slope to this point
	public final Comparator<Point> SLOPE_ORDER = new SlopeOrder(this);
	
	public static class SlopeOrder implements Comparator<Point> {
		Point ref;
		
		public SlopeOrder(Point ref) {
			this.ref = ref;
		}
		
		public int compare(Point a, Point b) {
			double m1=ref.slopeTo(a), m2=ref.slopeTo(b);
			if(m1>m2)
				return 1;
			if(m1<m2)
				return -1;
			
			// break ties in slope by comparing the two points
			return a.compareTo(b);
		}
	}        
	
	// x,y coordinates
	private final int x;
    private final int y;
	
	/**
	 * construct the point (x, y)
	 * @param x
	 * @param y
	 */
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	
	/**
	 * draw this point
	 */
	public void draw() {
		StdDraw.point(x, y);
	}
	
	
	/**
	 * draw the line segment from this point to that point
	 * @param that
	 */
	public void drawTo(Point that) {
		StdDraw.line(this.x, this.y, that.x, that.y);
	}
	
	
	/**
	 * string representation
	 */
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
	
	/**
	 * is this point lexicographically smaller than that point?
	 */
	public int compareTo(Point that) {
		if(this.y!=that.y)
			return this.y-that.y;
		if(this.x!=that.x)
			return this.x-that.x;
		return 0;
	}
	
	/**
	 * the slope between this point and that point
	 * @param that
	 * @return
	 */
	public double slopeTo(Point that) {
		if(this.x==that.x && this.y==that.y)
			return Double.NEGATIVE_INFINITY;
		if(this.x==that.x)
			return Double.POSITIVE_INFINITY;
		if(this.y==that.y)
			return 0.0f;
		
		return ((double)that.y - this.y)/((double)that.x - this.x);
	}
}