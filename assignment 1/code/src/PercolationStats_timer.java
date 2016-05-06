import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;




/**
 * Class that runs Percolation on an N*N grid and collects/reports statistics
 * on the number of open sites needed for the grid to percolate
 * 
 * @author ckingsley
 *
 */
public class PercolationStats_timer {
	int N, T;
	double[] fracOpenSites;
	
	/**
	 * perform T independent computational experiments on an N-by-N grid
	 * @param N
	 * @param T
	 */
	public PercolationStats_timer(int _N, int _T) {
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
	throws IllegalArgumentException, IOException {
		
		int N=200, T = 100;
		
		PercolationStats_timer percStats = new PercolationStats_timer(N, T);
		BufferedWriter outfile = new BufferedWriter(new FileWriter("runtime.txt"));
		System.out.println("starting");
		
		for(int N_iter=3150;N_iter<=3200;N_iter=N_iter+50) {
			Stopwatch runTime = new Stopwatch();
			
			for(int i=0;i<T;i++) {
				Percolation_WQUFPC perc = new Percolation_WQUFPC(N_iter);
				
				while(true) {
					perc.openRandomSite();
					
					if(perc.percolates()) {
						//System.out.println("percolates!!! " + perc.getFractionOpen());
						break;
					}
				}
				
				percStats.fracOpenSites[i] = perc.getFractionOpen();
			}
			outfile.write(N_iter + "\t" + runTime.elapsedTime() + "\n");
			System.out.println(N_iter + " \t" + runTime.elapsedTime());
//			System.out.println("mean:\t" + StdStats.mean(percStats.fracOpenSites));
//			System.out.println("stddev:\t" + StdStats.stddev(percStats.fracOpenSites));
//			System.out.println("95% confidence interval:\t" + percStats.confidenceLo() +", " + percStats.confidenceHi());
//			System.out.println("running time: " + runTime.elapsedTime() + " seconds");
			
		}
		
		outfile.close();
		
	}

}

