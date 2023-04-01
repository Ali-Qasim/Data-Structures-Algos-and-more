import java.util.*;

/*
   AIplayer is a class that contains the methods for implementing the minimax search for playing the TicTacToe game.
*/
class AIplayer { 
    List<Point> availablePoints; //an instance of the List class, a list of Point objects
    List<PointsAndScores> rootsChildrenScores; //an instance of the List class, a list of PointsAndScores objects holding the available moves and their values at the root of the search tree, i.e., the current game board.
    Board b = new Board(); //an instance of the Board class
    
    //constructor
    public AIplayer() {
    }
    
    //method for returning the best move, the position that has the maximum value among all the available positions 
    public Point returnBestMove() {
        int MAX = -100000; //The scores are not negative 
        int best = -1; //The index for available positions are not negative

        for (int i = 0; i < rootsChildrenScores.size(); ++i) { 
            if (MAX < rootsChildrenScores.get(i).score) {
                MAX = rootsChildrenScores.get(i).score;
                best = i;
            }
        }
        return rootsChildrenScores.get(best).point; //Return the position that has the maximum value among all the available positions
    }

    //method for returning the index of the minimum value in a list
    public int returnMin(List<Integer> list) {
        int min = Integer.MAX_VALUE;
        int index = -1;
        
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) < min) {
                min = list.get(i);
                index = i;
            }
        }
        return list.get(index);
    }

    //method for returning the index of the maximum value in a list
    public int returnMax(List<Integer> list) {

        int max = Integer.MIN_VALUE;
        int index = -1;
        
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) > max) {
                max = list.get(i);
                index = i;
            }
        }
        return list.get(index);
    }
 
    //method for calling minimax search, which takes depth, turn of play, and board as arguments.
    public void callMinimax(int depth, int turn, Board b){

        rootsChildrenScores = new ArrayList<>(); 
        minimax(depth, turn, Integer.MIN_VALUE, Integer.MAX_VALUE, b); //method for implementing the minimax search algorithm with alpha-beta pruning

    }
    
    /*
       minimax is a method for implementing the minimax search algorithm with alpha-beta pruning in a recursive manner, 
       which takes depth, turn of play, alpha, beta, and board as arguments. 
       Set depth=0 for the current game position that is the root of the search tree.
       turn=1 represents the AI player's turn to play whilst turn=2 represents the user's turn to play.
       The AI player is player 'X' and the user is player 'O'. 
       This method is the most important component of the TicTacToe program.
    */
    public int minimax(int depth, int turn, int alpha, int beta, Board b) { //added alpha, beta for alpha-beta pruning
        if (b.hasXWon()) return 1; //If player 'X' has won, the minimax search reaches a win endgame and returns 1 as the value of this endgame position
        if (b.hasOWon()) return -1; //If player 'O' has won, the minimax search reaches a loss endgame and returns -1 as the value of this endgame position
        List<Point> pointsAvailable = b.getAvailablePoints(); //Get a list of available moves on the current board
        if (pointsAvailable.isEmpty()) return 0; //If there is no available move, the minimax search reaches a draw endgame and returns 0 as the value of this endgame position.

        List<Integer> scores = new ArrayList<>(); //an instance of the List class, a list of integer scores holding the values of all the game positions in the next depth
        
        int temp;
        if (turn ==1) temp = Integer.MIN_VALUE; //added for alpha-beta pruning
        else temp = Integer.MAX_VALUE; //added for alpha-beta pruning
            
        for (int i = 0; i < pointsAvailable.size(); ++i) { //for each available move (position) ... 
            Point point = pointsAvailable.get(i); //Choose an available position in natural order to make a move 

            if (turn == 1) { //if it is the AI player's turn to play ...

                b.placeAMove(point, 1); //Make the chosen move to obtain a new board, i.e., a new game position in the next depth
                int currentScore = minimax(depth + 1, 2, alpha, beta, b); //Recursively call the minimax method with the new board, the corresponding depth and the user's turn as arguments. The recursion ends when reaching an endgame position. 
                scores.add(currentScore); //Add the value of the game position to the list of scores
                temp = Math.max(temp, currentScore); //added for alpha-beta pruning
                alpha = Math.max(alpha, temp); //added for alpha-beta pruning\

                if (depth == 0) //If it is at the root, add the chosen move and the corresponding value (score) to the list rootsCheldrenScores

                    rootsChildrenScores.add(new PointsAndScores(currentScore, point));

            } else if (turn == 2) { //if it is the user's turn to play ...

                b.placeAMove(point, 2); //Make the chosen move to obtain a new board, i.e., a new game position in the next depth
                int currentScore = minimax(depth + 1, 1, alpha, beta, b); //Recursively call the minimax method with the new board, the corresponding depth and the user's turn as arguments. The recursion ends when reaching an endgame position. 
                scores.add(currentScore);  
                temp = Math.min(temp, currentScore); //added for alpha-beta pruning
                beta = Math.min(beta, temp); //added for alpha-beta pruning
                //Recursively call the minimax method with the new board, the corresponding depth and the AI player's turn as arguments. The recursion ends when reaching an endgame position.
                //Add the value of the game position to the list of scores
            }

            b.placeAMove(point, 0); //Clear the chosen position after its value has been obtained by the minimax search.
            if (alpha >= beta) { //Check whether alpha is equal to or greater than beta. If so, there is no need to valuate the remaining game positions at this depth.

//                temp = pointsAvailable.size()-i-1;
//                System.out.println("Number of nodes that have not been evaluated here : "+ temp); //
//
                break;
            }
        }
        return turn == 1 ? returnMax(scores) : returnMin(scores); //If it is AI player's turn, return the maximum value. Otherwise, return the minimum value.
    }    
}
