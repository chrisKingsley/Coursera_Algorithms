public class BoidPQNode implements Comparable<BoidPQNode>{
	Boid b;
	double priority;

	public BoidPQNode(Boid b, double priority)
	{
		this.b = b;
		this.priority = priority;
	}

	public Boid b()
	{
		return b;
	}

	public int compareTo(BoidPQNode that)
	{
		if (priority < that.priority) return -1;
		if (priority > that.priority) return +1;
		return 0;
	}
}