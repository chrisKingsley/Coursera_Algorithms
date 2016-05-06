public class Hawk {
	public Point2D position;
	public JVector velocity;

	public Hawk(double x, double y)
	{
		position = new Point2D(x, y);
		velocity = new JVector(2); //start with zero velocity
	}


	public double x()
	{
		return position.x();
	}

	public double y()
	{
		return position.y();
	}

	public JVector returnToWorld()
	{
		JVector requestedVector = new JVector(2);

		JVector center = new JVector(new double[] {0.5, 0.5});


		JVector myPosition = new JVector(new double[]{x(), y()});
		requestedVector = center.minus(myPosition);

		return requestedVector;		
	}

	public JVector eatBoid(Boid boid)
	{
		JVector requestedVector = new JVector(2);

		JVector boidPosition = new JVector(new double[] {boid.x(), boid.y()});

		JVector myPosition = new JVector(new double[]{x(), y()});
		requestedVector = boidPosition.minus(myPosition);

		return requestedVector;		
	}			

	public JVector updatePositionAndVelocity(Boid nearest)
	{
		position.x = position.x + velocity.cartesian(0);
		position.y = position.y + velocity.cartesian(1);

		JVector desire = eatBoid(nearest).direction().times(0.0001);
		velocity = velocity.plus(desire);
		return desire;
	}

	public void draw()
	{
		StdDraw.setPenColor(StdDraw.RED);
		StdDraw.point(x(), y());
	}

}