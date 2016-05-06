import java.util.Arrays;

public class Brute {
	
	private static int numLines = 0;
	
	private static void checkPointsSlopes(Point[] points, int i, int j, int k, int l) {
		double m1 = points[i].slopeTo(points[j]);
		double m2 = points[i].slopeTo(points[k]);
		double m3 = points[i].slopeTo(points[l]);
		if(m1==m2 && m1==m2 && m1==m3) {
			System.out.printf("%s -> %s -> %s -> %s\n",
					points[i].toString(), points[j].toString(),
					points[k].toString(), points[l].toString());
			points[i].drawTo(points[l]);
			numLines++;
		}
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if(args.length==0)
			throw new java.lang.IllegalArgumentException("Must specify input filename");
		
		// setup drawing
		StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.setPenRadius(0.015);
		StdDraw.setXscale(0, 32768);
		StdDraw.setYscale(0, 32768);
	
		
		// load point coordinates from input file
		In in = new In(args[0]);
		
		int numPoints = in.readInt();
		Point[] points = new Point[numPoints];
		
		// add points to array, plot and sort
		for(int i=0;i<numPoints;i++) {
			int x = in.readInt();
			int y = in.readInt();
			points[i] = new Point(x,y);
			points[i].draw();
		}
		Arrays.sort(points);
		
		
		// check collinearity among combinations of 4 points
		StdDraw.setPenRadius(0.005);
		StdDraw.setPenColor(StdDraw.BLACK);
		
		Stopwatch runTime = new Stopwatch();
		
		for(int i=0;i<numPoints;i++) {
			for(int j=i+1;j<numPoints;j++) {
				for(int k=j+1;k<numPoints;k++) {
					for(int l=k+1;l<numPoints;l++) {
						checkPointsSlopes(points, i, j, k, l);
					}
				}
			}
		}
		StdDraw.show(0);
		
		System.out.println("elapsed time: " + runTime.elapsedTime());
		System.out.println("number lines drawn:" + numLines);

	}

}
