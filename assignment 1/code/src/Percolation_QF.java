
public class Percolation_QF {
	private boolean[][] grid = null;
	private int n, topSite, bottomSite, numOpen=0;
	QuickFindUF unionFind, unionFind2;
	
	/**
	 * constructor
	 * @param N dimension of each axis of the grid
	 */
	public Percolation_QF(int N) {
		n = N;
		topSite = n*n;
		bottomSite = n*n+1;
		grid = new boolean[n][n];
		
		// set up two union find objects.  One with connections to top and
		// bottom virtual sites, and one with connections only to the top
		// virtual site to solve the "backwash" problem
		unionFind = new QuickFindUF(n*n+2);
		unionFind2 = new QuickFindUF(n*n+1);
		
		// connect sites in bottom row of grid to a single bottom site and top row
		// of grid to single top site (neither top nor bottom sites are in the grid
		// itself).  Grid percolates if top site and bottom site are connected.  For
		// the second union find object, connect to top row of sites only
		for(int i=1 ; i<=n ; i++) {
			unionFind.union(xy_to_1D(1,i), topSite);
			unionFind.union(xy_to_1D(n,i), bottomSite);
			unionFind2.union(xy_to_1D(1,i), topSite);
		}
		
	}
	
	
	/**
	 * returns the fraction of open sites in the grid
	 * @return fraction of open sites in the grid
	 */
	public double getFractionOpen() {
		return (double)numOpen/(n*n);
	}
	
	
	/**
	 * Checks that the array indices i and j are both in the
	 * range 1..n, inclusive
	 * @param i grid row number (in 1 to N)
	 * @param j grid column number (in 1 to N)
	 * @throws java.lang.IndexOutOfBoundsException
	 */
	private void checkArrayIndices(int i, int j)
	throws java.lang.IndexOutOfBoundsException {
		if(i<1 || i>n)
			throw new IndexOutOfBoundsException("index i " + i + " out of bounds");
		if(j<1 || j>n)
			throw new IndexOutOfBoundsException("index j " + j + " out of bounds");
	}
	
	
	/**
	 * Converts the 2D grid position i,j (each in [1,N]) to a 1D array position
	 * (in [0,N*N-1]) for use in the union/find algorithm
	 * @param i grid row number (in 1 to N)
	 * @param j grid column number (in 1 to N)
	 * @return 1D array position corresponding to 2D grid position
	 */
	private int xy_to_1D(int i, int j) {
		return(n*(i-1) + j-1);
	}
	
	
	/**
	 * open site (row i, column j) if it is not already
	 * @param i grid row number (in 1 to N)
	 * @param j grid column number (in 1 to N)
	 */
	public void open(int i, int j)
	throws java.lang.IndexOutOfBoundsException {
		checkArrayIndices(i,j);
			
		grid[i-1][j-1] = true;
		numOpen++;
		
		// perform union with adjacent sites if they are open
		if(i>1 && isOpen(i-1, j)) {
			unionFind.union(xy_to_1D(i,j), xy_to_1D(i-1,j));
			unionFind2.union(xy_to_1D(i,j), xy_to_1D(i-1,j));
		}
		if(i<n && isOpen(i+1, j)) {
			unionFind.union(xy_to_1D(i,j), xy_to_1D(i+1,j));
			unionFind2.union(xy_to_1D(i,j), xy_to_1D(i+1,j));
		}
		if(j>1 && isOpen(i, j-1)) {
			unionFind.union(xy_to_1D(i,j), xy_to_1D(i,j-1));
			unionFind2.union(xy_to_1D(i,j), xy_to_1D(i,j-1));
		}
		if(j<n && isOpen(i, j+1)) {
			unionFind.union(xy_to_1D(i,j), xy_to_1D(i,j+1));
			unionFind2.union(xy_to_1D(i,j), xy_to_1D(i,j+1));
		}
	}
	
	
	/**
	 * is site (row i, column j) open?
	 * @param i grid row number (in 1 to N)
	 * @param j grid column number (in 1 to N)
	 * @return true if the site at (i,j) has been opened
	 */
	public boolean isOpen(int i, int j)
	throws java.lang.IndexOutOfBoundsException {
		checkArrayIndices(i,j);
		
		return grid[i-1][j-1];
	}
	
	
	/**
	 * Selects a closed site in the 2D grid at random and calls the open() function
	 * to open it
	 */
	public void openRandomSite() {
		int i,j;
		
		// select a pair of numbers in the range [1..N] inclusive
		do {
			i = StdRandom.uniform(n) + 1;
			j = StdRandom.uniform(n) + 1;
		} while(isOpen(i,j));
		
		open(i,j);
	}
	
	
	/**
	 * is site (row i, column j) full?
	 * @param i grid row number (in 1 to N)
	 * @param j grid column number (in 1 to N)
	 * @return true if the site is open and can be connected to the top row
	 * of the grid through other open sites
	 */
	public boolean isFull(int i, int j)
	throws java.lang.IndexOutOfBoundsException {
		checkArrayIndices(i,j);
		
		// check that the site is open and can be connected to the top row
		// of sites through the virtual top site.  Use the second union find
		// object, which does not include connections to the virtual bottom site
		return isOpen(i, j) && unionFind2.connected(xy_to_1D(i,j), topSite);
	}
	
	
	/**
	 * does the system percolate?
	 * @return true if the system percolates (at least one site bottom row is
	 * connected to at least one site in the top row in the grid)
	 */
	public boolean percolates() {
		return unionFind.connected(topSite, bottomSite);
	}
}
