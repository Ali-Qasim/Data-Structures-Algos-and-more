import java.util.*;

/*
   Point is a simple class with a constructor and an int to string method  
*/
class Point {
    int x, y;

    //constructor that takes two integers as arguments to define a point
    public Point(int x, int y) { 
        this.x = x;
        this.y = y;
    }

    @Override
    //method for converting integer to string 
    public String toString() { 
        return "[" + (x+1) + ", " + (y+1) + "]";
    }
}

/*
   PointsAndScores is a simple class with a constructor   
*/
class PointsAndScores {
    int score;
    Point point;

    //constructor that takes a Point object and its score as arguments
    PointsAndScores(int score, Point point) { 
        this.score = score;
        this.point = point;
    }
}

/*
   Board is a class that contains methods for checking game status and position status, 
   getting available moves (empty positions on the board), placing a player's move at a given position,
   and displaying the current board.   
*/
class Board { 
    List<Point> availablePoints; //an instance of the List class, list of Point objects  
    Scanner scan = new Scanner(System.in); //an instance of the Scanner class
    int[][] board = new int[3][3]; //an integer array holding the 3x3 game positions

    //constructor
    public Board() {
    }

    //method for checking whether the game is over
    public boolean isGameOver() {
        return (hasXWon() || hasOWon() || getAvailablePoints().isEmpty());
    }

    //method for checking whether player 'X' has won
    public boolean hasXWon() {
        if ((board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] == 1) || (board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] == 1)) {
            return true;
        } //Check diagonal lines
        for (int i = 0; i < 3; ++i) {
            if (((board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] == 1)
                    || (board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] == 1))) {
                return true;
            }
        } //Check rows and columns
        return false;
    }

    //method for checking whether player 'O' has won 
    public boolean hasOWon() {
        if ((board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] == 2) || (board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] == 2)) {
             return true;
        } //Check diagonal lines
        for (int i = 0; i < 3; ++i) {
            if ((board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] == 2)
                    || (board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] == 2)) {
                return true;
            }
        } //Check rows and columns
        return false;
    }

    //method for getting available moves on the current board (empty positions on the board)
    public List<Point> getAvailablePoints() {
        availablePoints = new ArrayList<>();
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (board[i][j] == 0) {
                    availablePoints.add(new Point(i, j)); //Add an empty position to availablePoints 
                }
            }
        }
        return availablePoints;
    }
    
    //method for getting the status of a position
    public int getState(Point point){
    	return board[point.x][point.y];
    }

    //method for placing a player's move at a given position 
    public void placeAMove(Point point, int player) {
        board[point.x][point.y] = player;   
    }

    //method for displaying the current board
    public void displayBoard() {
        System.out.println();

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
 		if (board[i][j]==1)           
                    System.out.print("X ");
                else if (board[i][j]==2)
                    System.out.print("O ");
                else
                    System.out.print(". ");
            }
            System.out.println();
        }
    }
}
