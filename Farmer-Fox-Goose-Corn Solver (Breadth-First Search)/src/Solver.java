import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

/*
   Solver is a class that contains the methods used to search for and print solutions
   plus the data structures needed for the search.
 */

public class Solver {

    ArrayList<Node> unexpanded = new ArrayList<Node>(); // Holds unexpanded node list
    ArrayList<Node> expanded = new ArrayList<Node>();   // Holds expanded node list
    Node rootNode;                                      // Node representing initial state

    /*
       Solver is a constructor that sets up an instance of the class with a node corresponding
       to the initial state as the root node.
     */
    public Solver(char[] initialBoard) {
        GameState initialState = new GameState(initialBoard);
        rootNode = new Node(initialState);
    }

    /*
       The method solve searches for a solution. It implements a breadth first search.
       The problem asks for a solution with the minimum number of moves.
       Breadth first search is both complete and optimal with respect to number of moves.
       The PrintWriter argument is used to specify where the output should be directed.
     */
    public void solve(PrintWriter output) {
        unexpanded.add(rootNode);          // Initialise the unexpanded node list with the root node.
        while (unexpanded.size() > 0) {    // While there are nodes waiting to be expanded:
            Node n = unexpanded.get(0);    // Get the first item off the unexpanded node list
            //Node n = unexpanded.get(unexpanded.size()-1);    // Get the last item off the unexpanded node list
            expanded.add(n);               // Add it to the expanded node list
            unexpanded.remove(0);          // Remove it from the unexpanded node list
            //unexpanded.remove(unexpanded.size()-1);          // Remove it from the unexpanded node list
            if (n.state.isGoal()) {        // If the node represents goal state then
                reportSolution(n, output); // Write solution to a file and terminate search
                return;
            } else {                       // Otherwise, (i.e. if the node does not represent goal state) then

                ArrayList<GameState> moveList = n.state.possibleMoves();      // Get list of permitted moves

                for (GameState gs : moveList) {  			      // For each such move:

                    if ((Node.findNodeWithState(unexpanded, gs) == null) &&   // If it is not already on either

                            (Node.findNodeWithState(expanded, gs) == null)) { // expanded or unexpanded node list then

                        if (    ((gs.board[0] != gs.board[1]) &&        	      // If Corn and Goose are not together, and
                                (gs.board[1] != gs.board[2])) ||              // Goose and Fox are not together either 
                                ((gs.board[0] == gs.board[1]) &&	      // If Corn and Goose and Farmer are together
                                (gs.board[0] == gs.board[3])) ||
                                ((gs.board[1] == gs.board[2]) &&	      // If Goose and Fox and Farmer are together
                                (gs.board[1] == gs.board[3])))  {
                                    int newCost = n.getCost() + 1;            // Add it to the unexpanded node list.
                                    Node newNode = new Node(gs, n, newCost);  // The parent is the current node.
                                    unexpanded.add(newNode);
                        }
                    }
                }
            }
        }
        output.println("No solution found");
    }

    /*
       printSolution is a recursive method that prints all the states in a solution.
       It uses the parent links to trace from the goal to the initial state then prints
       each state as the recursion unwinds.
       Node n should be a node representing the goal state.
       The PrintWriter argument is used to specify where the output should be directed.
     */
    public void printSolution(Node n, PrintWriter output) {
        if (n.parent != null) printSolution(n.parent, output);
        output.println(n.state);
    }

    /*
       reportSolution prints the solution together with statistics on the number of moves
       and the number of expanded and unexpanded nodes.
       The Node argument n should be a node representing the goal state.
       The PrintWriter argument is used to specify where the output should be directed.
     */
    public void reportSolution(Node n, PrintWriter output) {

        output.println("Solution found!");
        printSolution(n, output);
        output.println(n.getCost() + " Moves");
        output.println("Nodes expanded: " + this.expanded.size());
        output.println("Nodes unexpanded: " + this.unexpanded.size());
        output.println();

    }


    public static void main(String[] args) throws Exception {
        Solver problem = new Solver(GameState.INITIAL_BOARD);  // Set up the problem to be solved.
        File outFile = new File("output.txt");                 // Create a file as the destination for output
        PrintWriter output = new PrintWriter(outFile);         // Create a PrintWriter for that file
        problem.solve(output);                                 // Search for and print the solution
        output.close();                                        // Close the PrintWriter (to ensure output is produced).
    }
}
