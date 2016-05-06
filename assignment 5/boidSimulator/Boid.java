public class Boid implements Comparable<Boid>{	
	Point2D position;
	JVector velocity;



	public Boid(double x, double y) {
		position = new Point2D(x, y);
		velocity = new JVector(2);
	}

	public Boid(double x, double y, double xvel, double yvel) {
		position = new Point2D(x, y);
		velocity = new JVector(new double[]{xvel, yvel});
	}

	public Point2D position() {
		return new Point2D(position.x(), position.y());
	}

	public double x() {
		return position.x();
	}

	public double y() {
		return position.y();
	}


	public JVector avoidCollision(Iterable<Boid> neighbors)
	{
		JVector requestedVector = new JVector(2);

		JVector myPosition = new JVector(new double[]{x(), y()});
		int i = 0;
		int cnt = 0;
		for (Boid b : neighbors)
		{
			cnt++;
			JVector neighborPosition = new JVector(new double[] {b.x(), b.y()});

			double distanceTo = myPosition.distanceTo(neighborPosition);
			//don't count self
			if (distanceTo == 0.0)
				break;

			JVector avoidanceVector = myPosition.minus(neighborPosition);
			JVector scaledAvoidanceVector = avoidanceVector.times(1.0 / distanceTo);			
			requestedVector = requestedVector.plus(scaledAvoidanceVector);
		}

		return requestedVector;
	}


	public JVector avoidCollision(Hawk hawk)
	{
		JVector requestedVector = new JVector(2);

		JVector myPosition = new JVector(new double[]{x(), y()});
		int i = 0;

			JVector hawkPosition = new JVector(new double[] {hawk.x(), hawk.y()});

			double distanceTo = myPosition.distanceTo(hawkPosition);
			//don't count self
			if (distanceTo == 0.0)
				return new JVector(2);

			JVector avoidanceVector = myPosition.minus(hawkPosition);
			JVector scaledAvoidanceVector = avoidanceVector.times(1.0 / distanceTo);			
			requestedVector = requestedVector.plus(scaledAvoidanceVector);


		return requestedVector;
	}
	public JVector matchVelocity(Iterable<Boid> neighbors)
	{
		JVector requestedVector = new JVector(2);

		for (Boid b : neighbors)
		{
			JVector neighborVelocity = new JVector(b.getVelocity());
			JVector matchingVector = neighborVelocity.minus(velocity);			
			requestedVector = requestedVector.plus(matchingVector);
		}

		return requestedVector;		
	}

	public JVector plungeDeeper(Iterable<Boid> neighbors)
	{
		JVector requestedVector = new JVector(2);

		JVector centroid = new JVector(2);
		double neighborCnt = 0;

		for (Boid b : neighbors)
		{
			JVector neighborPosition = new JVector(new double[] {b.x(), b.y()});
			centroid = centroid.plus(neighborPosition);			
			neighborCnt++;
		}

		centroid = centroid.times(1.0 / neighborCnt);
		Point2D centroidPoint = new Point2D(centroid.cartesian(0), centroid.cartesian(1));

		JVector myPosition = new JVector(new double[]{x(), y()});
		requestedVector = centroid.minus(myPosition);

		return requestedVector;		
	}

	public JVector returnToWorld()
	{
		JVector requestedVector = new JVector(2);

		JVector center = new JVector(new double[] {0.5, 0.5});


		JVector myPosition = new JVector(new double[]{x(), y()});
		requestedVector = center.minus(myPosition);

		return requestedVector;		
	}	

	public JVector desiredAcceleration(Iterable<Boid> neighbors, Hawk hawk)
	{
		JVector avoidanceVector = avoidCollision(neighbors).times(0.01);
		JVector matchingVector = matchVelocity(neighbors);
		JVector plungingVector = plungeDeeper(neighbors);
		JVector returnVector = returnToWorld().times(0.05);
		JVector hawkAvoidanceVector = avoidCollision(hawk).times(0.01);

		JVector desired = new JVector(2);
		desired = desired.plus(avoidanceVector);
		desired = desired.plus(matchingVector);
		desired = desired.plus(plungingVector);
		desired = desired.plus(hawkAvoidanceVector);
		desired = desired.plus(returnVector);
		if (desired.magnitude() == 0.0)
			return desired;

		return desired.direction().times(0.0001);
	}

	public void draw()
	{
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.point(x(), y());
	}

	public JVector getVelocity()
	{
		return new JVector(velocity);
	}	

	public String toString() {
		return "" + x() + " " + y() + " " + " " + velocity.toString();
	}

	public JVector updatePositionAndVelocity(Iterable<Boid> neighbors, Hawk hawk)
	{
		double x = x() + velocity.cartesian(0);
		double y = y() + velocity.cartesian(1);
		position = new Point2D(x, y);
		JVector desire = desiredAcceleration(neighbors, hawk);
		velocity = velocity.plus(desire);
		return desire;
	}

	public double distanceSquaredTo(Boid b)	{
		return position.distanceSquaredTo(b.position());
	}

    // compare by y-coordinate, breaking ties by x-coordinate
    public int compareTo(Boid that) {
        if (this.position.y < that.position.y) return -1;
        if (this.position.y > that.position.y) return +1;
        if (this.position.x < that.position.x) return -1;
        if (this.position.x > that.position.x) return +1;
        return 0;
    }

	public static void main(String[] args)
	{

	}
}