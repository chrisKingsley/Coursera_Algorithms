public class Checker {
	int ticksPerItem; 
	int nextItemTick;

	public Checker(int ticksPerItem)
	{
		this.ticksPerItem = ticksPerItem;
		nextItemTick = ticksPerItem;
	}

	public boolean processItem()
	{
		nextItemTick--;
		if (nextItemTick == 0)
		{
			nextItemTick = ticksPerItem;
			return true;
		}

		return false;
	}
	
}