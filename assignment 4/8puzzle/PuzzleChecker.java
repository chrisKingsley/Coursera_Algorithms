/*************************************************************************
 *  Compilation:  javac PuzzleChecker.java
 *  Execution:    java PuzzleChecker filename1.txt filename2.txt ...
 *  Dependencies: Board.java Solver.java In.java
 *
 *  This program creates an initial board from each filename specified
 *  on the command line and finds the minimum number of moves to
 *  reach the goal state.
 *
 *  % java PuzzleChecker puzzle[0-9][0-9].txt
 *  puzzle00.txt: 0
 *  puzzle01.txt: 1
 *  puzzle02.txt: 2
 *  puzzle03.txt: 3
 *  puzzle04.txt: 4
 *  puzzle05.txt: 5
 *  puzzle06.txt: 6
 *  ...
 *  puzzle47.txt: 47
 *  puzzle48.txt: 48
 *  puzzle49.txt: 49
 *  puzzle50.txt: 50
 *
 * 
 *************************************************************************/

public class PuzzleChecker {

    public static void main(String[] args) {

        // for each command-line argument
        for (String filename : args) {

            // read in the board specified in the filename
            In in = new In(filename);
            int N = in.readInt();
            int[][] blocks = new int[N][N];
            for (int i = 0; i < N; i++)
                for (int j = 0; j < N; j++)
                    blocks[i][j] = in.readInt();
            Board initial = new Board(blocks);

            // check if puzzle is solvable; if so, solve it and output solution
            if (initial.isSolvable()) {
                Solver solver = new Solver(initial);
                StdOut.println("Minimum number of moves = " + solver.moves());
                for (Board board : solver.solution())
                    StdOut.println(board);
            }

            // if not, report unsolvable
            else {
                StdOut.println("Unsolvable puzzle");
            }
        }
    }
}
