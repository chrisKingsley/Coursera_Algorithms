/*************************************************************************
 *  Compilation:  javac PointSequence.java
 *
 *  Container class for a Point array to simplify syntax and debugging for
 *  collinear assignment.
 *
 *  jhug@cs.princeton.edu
 *
 *************************************************************************/

public final class PointSequence {
   private final Point[] points;

   public PointSequence(Point[] points)
   {
		if (points == null)
			 throw new RuntimeException("null passed to PointSequence constructor.");

		if (points.length < 2)
     throw new RuntimeException("PointSequence must consist of at least 2 points.");

		//exception if 1 or fewer points or if any points are null
		this.points = new Point[points.length];

		for (int i = 0; i < points.length; i++)
		{
			if (points[i] == null)
            throw new RuntimeException("Point #" + i + " passed to PointSequence constructor is null.");
			this.points[i] = points[i];
		}   		
   }

   public String toString()
   {
	  StringBuilder sb = new StringBuilder();
      sb.append(points[0]);
      for (int i = 1; i < points.length; i++)
         sb.append(" -> " + points[i]);
              
      return sb.toString();
   }

   public void draw()
   {
      int N = points.length;
      for (int i = 0; i < N - 1; i++) 
      {
        points[i].drawTo(points[i+1]);
      }
   }

   public int numberOfPoints()
   {
      return points.length;
   }

   public Point get(int i)
   {
      return points[i];
   }

} 
