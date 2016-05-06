public class BoidSim {
	public static void main(String[] args)
	{		
		Hawk hawk = new Hawk(0.5, 0.3);
		StdDraw.show(20);
		int NUM_BOIDS = 1000;
		int FRIENDS = 10;
		Boid[] boids = new Boid[NUM_BOIDS];
		double meanX, meanY, mvX, mvY;
		for (int i = 0; i < NUM_BOIDS; i++)
		{
			double startX = StdRandom.uniform();
			double startY = StdRandom.uniform();
			double velX = (StdRandom.uniform() - 0.5)/1000;
			double velY = (StdRandom.uniform() - 0.5)/1000;
			boids[i] = new Boid(startX, startY, velX, velY);
		}

		StdDraw.setPenRadius(0.01);


		while(true)
		{
			for (int i = 0; i < NUM_BOIDS; i++)
				boids[i].draw();
			hawk.draw();


			BoidKdTree bkd = new BoidKdTree();
			for (int i = 0; i < NUM_BOIDS; i++)			
			{
				bkd.insert(boids[i]);
			}
			
			for (int i = 0; i < NUM_BOIDS; i++)
			{
				Iterable<Boid> kNearest = bkd.kNearest(boids[i], FRIENDS);
				boids[i].updatePositionAndVelocity(kNearest, hawk);
			}

			hawk.updatePositionAndVelocity(bkd.nearest(new Boid(hawk.x(), hawk.y())));
			StdDraw.show(20);
			StdDraw.clear();

		}
	}
}