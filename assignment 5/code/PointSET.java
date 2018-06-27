import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
		for (Point2D p : tree) {
			StdDraw.point(p.x(), p.y());
		}
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
	
	/**
	 * Test the range search functionality of the PointSET class
	 */
	private void testRangeSearch() {
		double x0 = 0.0, y0 = 0.0;      // initial endpoint of rectangle
        double x1 = 0.0, y1 = 0.0;      // current location of mouse
        boolean isDragging = false;     // is the user dragging a rectangle

        // draw the points
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        draw();

        while (true) {
            StdDraw.show(40);

            // user starts to drag a rectangle
            if (StdDraw.mousePressed() && !isDragging) {
                x0 = StdDraw.mouseX();
                y0 = StdDraw.mouseY();
                isDragging = true;
                continue;
            }

            // user is dragging a rectangle
            else if (StdDraw.mousePressed() && isDragging) {
                x1 = StdDraw.mouseX();
                y1 = StdDraw.mouseY();
                continue;
            }

            // mouse no longer pressed
            else if (!StdDraw.mousePressed() && isDragging) {
                isDragging = false;
            }


            RectHV rect = new RectHV(Math.min(x0, x1), Math.min(y0, y1),
                                     Math.max(x0, x1), Math.max(y0, y1));
            // draw the points
            StdDraw.clear();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(.01);
            draw();

            // draw the rectangle
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius();
            rect.draw();

            // draw the range search results for brute-force data structure in red
            StdDraw.setPenRadius(.03);
            StdDraw.setPenColor(StdDraw.RED);
            for (Point2D p : range(rect))
                p.draw();

            StdDraw.show(40);
        }
	}
	
	/**
	 * main method for testing
	 * @param args
	 */
	public static void main(String[] args) {
		PointSET brute = new PointSET();
		
		try {
			BufferedReader infile = new BufferedReader(new FileReader(args[0]));
			while(infile.ready()) {
				String[] tokens = infile.readLine().trim().split(" ");
				Point2D p = new Point2D(Double.parseDouble(tokens[0]), Double.parseDouble(tokens[1]));
				brute.insert(p);
			}
			infile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// draw the points
        //brute.testNearestNeighbor();
        brute.testRangeSearch();
	}
}