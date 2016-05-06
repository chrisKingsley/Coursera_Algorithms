import java.util.ArrayList;

/**
 * @author ckingsley
 *
 */
public class Board implements Comparable<Board> {
	private Board prev;
	private char[][] tiles;
	private int N, manhattanDist, numMoves, x0, y0;
	
	// construct a board from an N-by-N array of blocks
	// (where blocks[i][j] = block in row i, column j)
	public Board(char[][] blocks) {
		tiles = blocks;
		prev = null;
		N = blocks.length;
		numMoves = 0;
		manhattanDist = manhattan();
		
		outerloop:
		for(int i=0;i<N;i++) {
			for(int j=0; j<N; j++) {
				if((int)tiles[i][j]==0) {
					x0=i;
					y0=j;
					break outerloop;
				}
			}
		}
	}
	
	public int getNumMoves() {
		return numMoves;
	}
    
	// board dimension N
	public int dimension() {
		return N;
	}
	
	public Board getPrevious() {
		return prev;
	}
	
	
	// compareTo function to implement Comparable
	public int compareTo(Board other) {
		return numMoves + manhattanDist - other.numMoves - other.manhattanDist;
	}
	
	
	// returns Hamming distance (number of blocks out of place)
	public int hamming() {
		int index = 1, numOutOfPlace = 0;
		
		for(int i=0; i<dimension(); i++) {
			for(int j=0; j<dimension(); j++) {
				int tile = (int)tiles[i][j];
				if(tile!=index++ && tile!=0)
					numOutOfPlace++;
			}
		}
		
		return numOutOfPlace;
	}
	
	
	// sum of Manhattan distances between blocks and goal
	public int manhattan() {
		int index=1, manhattanDist=0;
		
		for(int i=0; i<N; i++) {
			for(int j=0; j<N; j++) {
				int tile = (int)tiles[i][j];
				if(tile!=0) {
					manhattanDist += Math.abs((index-1)/N - (tile-1)/N) +
						Math.abs((index-1) % N - (tile-1) % N);
				}
				index++;
			}
		}
		
		return manhattanDist;
	}
	
	
	// is this board the goal board?
	public boolean isGoal() {
		int index=1;
		
		for(int i=0; i<dimension(); i++) {
			for(int j=0; j<dimension(); j++) {
				int tile = (int)tiles[i][j];
				if((index==N*N && tile!=0) || 
						(index<N*N && tile!=index)) {
					return(false);
				}
				index++;
			}
		}
		return true;
	}
	
	
	// return a board obtained by exchanging two adjacent non-zero blocks
	// in the same row
	public Board twin() {
		char[][] newTiles = new char[N][N];
		
		for(int i=0; i<N; i++) {
			for(int j=0; j<N; j++) {
				newTiles[i][j] = tiles[i][j];
			}
		}
		
		// swap two non-zero entries in one row
		for(int i=0; i<N; i++) {
			int tile1 = (int)tiles[i][0], tile2 = (int)tiles[i][1];
			if(tile1!=0 && tile2!=0) {
				newTiles[i][0] = tiles[i][1];
				newTiles[i][1] = tiles[i][0];
				break;
			}
		}
		
		Board board = new Board(newTiles);
		board.prev = prev;
		board.numMoves = numMoves;
		
		return board;
	}
	
	
	// does this board equal y?
	public boolean equals(Object other) {
		if(other==this) return true;
		if(other==null) return false;
		if(other.getClass()!=this.getClass()) return false;
		
		Board that = (Board)other;
		if(that.N!=N) return false;
		
		for(int i=0; i<N; i++) {
			for(int j=0; j<N; j++) {
				if(tiles[i][j]!=that.tiles[i][j]) return false;
			}
		}
		
		return true;
	}
	
	
	// adds adjacent boards to the passed ArrayList as long as the moves are
	// in bounds and the new board does not equal the previous
	private void addAdjacentBoard(ArrayList<Board> neighbors, int xInc, int yInc) {
		if(x0+xInc>=0 && x0+xInc<N && y0+yInc>=0 && y0+yInc<N) {
			char[][] newTiles = new char[N][N];
			
			for(int i=0; i<N; i++) {
				for(int j=0; j<N; j++) {
					newTiles[i][j] = tiles[i][j];
				}
			}
			
			newTiles[x0][y0] = tiles[x0+xInc][y0+yInc];
			newTiles[x0+xInc][y0+yInc] = 0;
			Board newBoard = new Board(newTiles);
			newBoard.prev = this;
			newBoard.numMoves = numMoves + 1;
			newBoard.x0 = x0 + xInc;
			newBoard.y0 = y0 + yInc;
			
			if(!newBoard.equals(this.prev))
				neighbors.add(newBoard);
		}
	}
	
	
	// all neighboring boards
	public Iterable<Board> neighbors() {
		ArrayList<Board> neighbors = new ArrayList<Board>();
		
		// add neighbors to list
		addAdjacentBoard(neighbors, -1, 0);
		addAdjacentBoard(neighbors, 1, 0);
		addAdjacentBoard(neighbors, 0, 1);
		addAdjacentBoard(neighbors, 0, -1);
		
		return neighbors;
	}
	
	
	// string representation
	public String toString() {
	    StringBuilder s = new StringBuilder();
	    s.append("moves:" + numMoves + " dist:" + manhattanDist +
	    		 " priority:" + (numMoves + manhattanDist) + "\n");
	    
	    for (int i = 0; i < N; i++) {
	        for (int j = 0; j < N; j++) {
	            s.append(String.format("%2d ", (int)tiles[i][j]));
	        }
	        s.append("\n");
	    }
	    
	    return s.toString();
	}
}
