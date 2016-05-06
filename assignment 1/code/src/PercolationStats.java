

/**
 * Class that runs Percolation on an N*N grid and collects/reports statistics
 * on the number of open sites needed for the grid to percolate
 * 
 * @author ckingsley
 *
 */
public class PercolationStats {
	int N, T;
	double[] fracOpenSites;
	
	/**
	 * perform T independent computational experiments on an N-by-N grid
	 * @param N
	 * @param T
	 */
	public PercolationStats(int _N, int _T) {
		this.N = _N;
		this.T = _T;
		fracOpenSites = new double[T];
	}
	
	
	/**
	 * returns lower bound of the 95% confidence interval
	 * @return
	 */
	public double confidenceLo() {
		return StdStats.mean(fracOpenSites) - 1.96*StdStats.stddev(fracOpenSites)/Math.sqrt(T);
	}
	

	/**
	 * returns upper bound of the 95% confidence interval
	 * @return
	 */
	public double confidenceHi() {
		return StdStats.mean(fracOpenSites) + 1.96*StdStats.stddev(fracOpenSites)/Math.sqrt(T);
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	throws java.lang.IllegalArgumentException {
		if(args.length < 2)
			throw new IllegalArgumentException("Two arguments required: N (grid dimension) and T (number iterations)");
		
		int N = Integer.parseInt(args[0]);
		int T = Integer.parseInt(args[1]);
		if(N<=1)
			throw new IllegalArgumentException("First argument N must be >= 1");
		if(T<=1)
			throw new IllegalArgumentException("Second argument T must be >= 1");
		
		boolean displayGrid = false;
		int delay=100;
		if(args.length==3) {
			displayGrid = true;
			delay = Integer.parseInt(args[2]);
		}
		
		Stopwatch runTime = new Stopwatch();
		PercolationStats percStats = new PercolationStats(N, T);
		
		for(int i=0;i<T;i++) {
			Percolation perc = new Percolation(N);
			
			// turn on animation mode if the user has so specified
	        if(displayGrid) {
	        	StdDraw.show(0);
	        	PercolationVisualizer.draw(perc, N);
	        }
			
			while(true) {
				perc.openRandomSite();
				if(displayGrid) {
					PercolationVisualizer.draw(perc, N);
					StdDraw.show(delay);
				}
				
				if(perc.percolates()) {
					//System.out.println("percolates!!! " + perc.getFractionOpen());
					if(displayGrid)
						StdDraw.show(2000);
					break;
				}
			}
			
			percStats.fracOpenSites[i] = perc.getFractionOpen();
		}
		
		System.out.println("mean:\t" + StdStats.mean(percStats.fracOpenSites));
		System.out.println("stddev:\t" + StdStats.stddev(percStats.fracOpenSites));
		System.out.println("95% confidence interval:\t" + percStats.confidenceLo() +", " + percStats.confidenceHi());
		System.out.println("running time: " + runTime.elapsedTime() + " seconds");
	
	}

}
