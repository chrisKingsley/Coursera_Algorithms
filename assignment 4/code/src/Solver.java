/**
 * @author ckingsley
 *
 */
public class Solver {

	/**
	 * @param args
	 */
	private Board board;
	private MinPQ<Board> minPQ, minPQ_twin;
	public int numEnques, numDeques;
	
	// find a solution to the initial board (using the A* algorithm)
	public Solver(Board initial)  {
		board = initial;
		
		minPQ = new MinPQ<Board>();
		minPQ.insert(initial);
		
		minPQ_twin = new MinPQ<Board>();
		minPQ_twin.insert(initial.twin());
		
		numEnques = numDeques = 0;
	}
	
	
	// is the initial board solvable?
    public boolean isSolvable() {
    	while(true) {
    		board = minPQ.delMin();
//    		System.out.println("Min from queue");
//    		System.out.print(board);
//    		System.out.printf("moves:%d priority:%d\n\n", numInserts, board.priority);
    		if(board.isGoal()) {
    			break;
    		}
    		for(Board neighbor : board.neighbors()) {
    			minPQ.insert(neighbor);
    			numEnques++;
//    			System.out.print(neighbor);
//    			System.out.printf("moves:%d priority:%d\n\n", numInserts, neighbor.priority);
    		}
    		
    		board = minPQ_twin.delMin();
    		if(board.isGoal()) {
    			numDeques = -1;
    			break;
    		}
    		for(Board neighbor : board.neighbors()) {
    			minPQ_twin.insert(neighbor);
    		}
    		
    		numDeques++;
    	}
    	
    	return numDeques > -1;
    }
    
    
    // min number of moves to solve initial board; -1 if no solution
    public int moves() {
    	return board.getNumMoves();
    }
    
    // sequence of boards in a shortest solution; null if no solution
    public Iterable<Board> solution() {
    	if(numDeques == -1) return null;
    	
    	Stack<Board> boardList = new Stack<Board>();
    	
		while(board!=null) {
			boardList.push(board);
			board = board.getPrevious();
		}
    	
    	return boardList;
    }
    
    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        char[][] blocks = new char[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = (char)in.readInt();
        
        // solve the puzzle
        Board initial = new Board(blocks);
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
        	 StdOut.println("Minimum number of moves = " + solver.moves());
        	 for (Board board : solver.solution())
                StdOut.println(board);
           
            StdOut.printf("Num Enques %d   Num Deques %d\n", solver.numEnques,
            		solver.numDeques);
        }
    }

}
