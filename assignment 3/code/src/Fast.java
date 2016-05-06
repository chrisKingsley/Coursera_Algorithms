import java.util.Arrays;

/**
 * Class for fast identification of collinear points using sorting
 * @author Chris Kingsley
 *
 */
public class Fast {
	
	// minimum number of collinear points to be called a line
	private static final int MIN_NUM_POINTS = 4;
	private static int numLines = 0;
	
	/**
	 * Load points from input file
	 * @param infile input file
	 * @return sorted array of Point objects
	 */
	private static Point[] loadPoints(String infile) {
		In in = new In(infile);
		
		int numPoints = in.readInt();
		Point[] points = new Point[numPoints];
		
		for(int i=0;i<numPoints;i++) {
			int x = in.readInt();
			int y = in.readInt();
			points[i] = new Point(x,y);
		}
		Arrays.sort(points);
		
		return(points);
	}
	
	
	/**
	 * Setup drawing area and plot array of points
	 * @param points array of Point objects
	 */
	private static void setupPlot(Point[] points) {
		// setup drawing
		StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.setPenRadius(0.010);
		StdDraw.setXscale(0, 32768);
		StdDraw.setYscale(0, 32768);
		
		for(Point p:points){
			p.draw();
		}
		
		StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.005);
	}
	
	
	public static int getStopIndex(Point[] points, Point p, int start_idx) {
		double slope = p.slopeTo(points[start_idx]);
		
		for(int stop_idx=start_idx; stop_idx<points.length; stop_idx++) {
			if(p.slopeTo(points[stop_idx])!=slope) return stop_idx-1;
		}
		
		return points.length - 1;
	}
	
	public static boolean pointsOK(Point[] points, Point p, int start_idx, int stop_idx) {
		// check that number of points is sufficient
		if(stop_idx - start_idx + 1 < MIN_NUM_POINTS - 1)
			return false;
		
		// check that first two points are in ascending order
		return(p.compareTo(points[start_idx]) < 0);
	}
	
	
	/**
	 * Draw lines from collinear points and print out sequence of points
	 * @param points
	 * @param p
	 * @param start_idx
	 * @param stop_idx
	 */
	public static void outputPoints(Point[] points, Point p, int start_idx, int stop_idx) {
		p.drawTo(points[stop_idx]);
		numLines++;
		
		System.out.printf("%s", p);
		for(int i=start_idx; i<=stop_idx; i++)
			System.out.printf(" -> %s", points[i]);
		System.out.println();
	}
	
	
	/**
	 * Check whether point p initiates a set of collinear points.
	 * If so, draw line on screen and print sequence of points
	 * @param points array of Point objects
	 * @param p initial point
	 */
	public static void checkCollinearity(Point[] points, Point p) {
		int start_idx = 0;
		
		while(start_idx < points.length) {
			int stop_idx = getStopIndex(points, p, start_idx);
			if(pointsOK(points, p, start_idx, stop_idx))
				outputPoints(points, p, start_idx, stop_idx);
			start_idx = stop_idx + 1;
		}
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length==0)
			throw new java.lang.IllegalArgumentException("Must specify input filename");
		
		Point[] points = loadPoints(args[0]);
		setupPlot(points);
		
		Stopwatch runTime = new Stopwatch();
		for(Point p: points) {
			Arrays.sort(points, p.SLOPE_ORDER);
			checkCollinearity(points, p);
			
//			for(Point p1 : points) {
//				System.out.printf("%s %s %f\n",
//						p.toString(), p1.toString(), p.slopeTo(p1));
//			}
			
			Arrays.sort(points);
		}
		
		System.out.println("elapsed time: " + runTime.elapsedTime());
		System.out.println("number lines drawn:" + numLines);
		

	}

}
