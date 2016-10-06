import java.util.LinkedList;
import java.util.Scanner;
/**
 * Pentago Game Class.
 * Keeps track of the current board, heuristic value for each piece, and all prior moves.
 * @author Anh Tran
 */
public class Pentago {

    //Current game board
    private String board;
    //List of all prior moves
    private LinkedList<String> moves;
    //If the board signifies a win/tie or not.
    private boolean endgame;
    //Gives the winner of the game. 1 for W, 2 for B, 0 for a draw. Random flag otherwise
    private int winner;

    //Heuristic values for each piece.
    private int heuristicW;
    private int heuristicB;

    /**
     * Constructor. Creates initial empty gameboard.
     */
    public Pentago() {
        char[] temp = new char[36];
        for(int i = 0; i < temp.length; i++) {
            temp[i] = '.';
        }
        board = new String(temp);
        moves = new LinkedList<String>();

        endgame = false;
        winner = -1;

        heuristicW = 0;
        heuristicB = 0;
    }

    //Private constructor. Creates next state of the game board depending on the move.
    private Pentago(Pentago p1, String newBoard, String newMove, boolean newEndgame, int newWinner, int newHeuristicW, int newHeuristicB) {
        board = newBoard;
        moves = (LinkedList<String>)p1.moves.clone();
        p1.getMoves().removeLast();
        endgame = newEndgame;
        winner = newWinner;
        if(moves.size() % 2 == 0) {
            heuristicW = p1.getHeuristicW();
            heuristicB = p1.getHeuristicB() + newHeuristicB;
        }
        else {
            heuristicW = p1.getHeuristicW() + newHeuristicW;
            heuristicB = p1.getHeuristicB();
        }
    }

    /**
     * Returns the heuristic of the game board for the white piece.
     * @return Heuristic value for player 1.
     */
    public int getHeuristicW() {
        return heuristicW;
    }

    /**
     * Returns the heuristic of the game board for the black piece.
     * @return Heuristic value for player 2.
     */
    public int getHeuristicB() {
        return heuristicB;
    }

    /**
     * Returns the list of all prior moves.
     * @return LinkedList of moves.
     */
    public LinkedList<String> getMoves() {
        return moves;
    }

    /**
     * Returns int flag for winner value.
     * @return Returns 1 for player 1 win, 2 for player 2 win, 0 for a tie.
     */
    public int getWinner() {
        return winner;
    }

    /**
     * Gets the current game board.
     * @return Current game board as a String.
     */
    public String getBoard() {
        return board;
    }

    /**
     * Determines if the game has ended.
     * @return True if the game is in the end state. False otherwise.
     */
    public boolean isEndgame() {
        return endgame;
    }

    public void setPentago(Pentago p) {
        board = p.board;
        //casting with abandon
        moves = (LinkedList<String>)p.moves.clone();
        heuristicW = p.heuristicW;
        heuristicB = p.heuristicW;
        winner = p.winner;
        endgame = p.endgame;
    }

    //Rotates the game board left at the given quadrant.
    private String rotateLeft(int quadrant, String board) {
        char[] temp = board.toCharArray();
        int i = 0;
        int j = 0;
        switch (quadrant) {
            case 1: break;
            case 2: i += 3;
                j += 3;
                break;
            case 3: i += 18;
                j += 18;
                break;
            case 4: i += 21;
                j += 21;
                break;
        }
        temp[0 + i] = board.charAt(2 + j);
        temp[1 + i] = board.charAt(8 + j);
        temp[2 + j] = board.charAt(14 + j);
        temp[6 + i] = board.charAt(1 + j);
        temp[7 + i] = board.charAt(7 + j);
        temp[8 + i] = board.charAt(13 + j);
        temp[12 + i] = board.charAt(0 + j);
        temp[13 + i] = board.charAt(6 + j);
        temp[14 + i] = board.charAt(12 + j);

        return new String(temp);
    }

    //Rotates the game board right at the given quadrant.
    private String rotateRight(int quadrant, String board) {
        char[] temp = board.toCharArray();
        int i = 0;
        int j = 0;
        switch (quadrant) {
            case 1: break;
            case 2: i += 3;
                j += 3;
                break;
            case 3: i += 18;
                j += 18;
                break;
            case 4: i += 21;
                j += 21;
                break;
        }
        temp[0 + i] = board.charAt(12 + j);
        temp[1 + i] = board.charAt(6 + j);
        temp[2 + j] = board.charAt(0 + j);
        temp[6 + i] = board.charAt(13 + j);
        temp[7 + i] = board.charAt(7 + j);
        temp[8 + i] = board.charAt(1 + j);
        temp[12 + i] = board.charAt(14 + j);
        temp[13 + i] = board.charAt(8 + j);
        temp[14 + i] = board.charAt(2 + j);

        return new String(temp);
    }

    //Determines which game piece is put on the board.
    public char gamePiece() {
        if(moves.size() % 2 == 0)
            return 'b';
        else
            return 'w';
    }

    /**
     * Puts the game piece and rotates board.
     * @param move String signifying next move.
     * @return Game board of next move. Null if move is invalid.
     */
    public Pentago move(String move) {
        //System.out.println(move);
        //Checks if gameboard is full.
        if(moves.size() == 36) {
            endgame = true;
            return this;
        }
        int board = 0;
        int position = 0;
        int quadrant = 0;
        char rotate = 0;
        //Scans move and validates input
        Scanner scan = new Scanner(move).useDelimiter("\\W");
        moves.add(move);
        try {
            board = scan.nextInt();
            if(board <= 0 || board > 4)
                throw new Exception("Invalid board selection");

            position = scan.nextInt();
            if(position <= 0 || position > 9)
                throw new Exception("Invalid position");

            String bd = scan.next();
            quadrant = bd.charAt(0) - '0';
            if(quadrant <= 0 || quadrant > 4)
                throw new Exception("Invalid board to rotate");

            rotate = bd.charAt(1);
            if(rotate != 'L')
                if(rotate != 'R')
                    throw new Exception("Invalid rotation selection");
        } catch(Exception e) {
            System.out.println(e + ". No moves made");
            moves.removeLast();
            return null;
        }
        //Calculates where to put the piece on the game board.
        position--;
        int div = position / 3;
        int rem = position % 3;
        int pos = 0;
        switch (board) {
            case 1: pos = div * 6 + rem;
                break;
            case 2: pos = ((div * 6) + 3) + rem;
                break;
            case 3: pos = ((div * 6) + 18) + rem;
                break;
            case 4: pos = ((div * 6) + 21) + rem;
                break;
        }
        //Checks if position is already occupied.
        if(this.board.charAt(pos) != '.') {
            //System.out.println(this.board.charAt(pos));
            //System.out.println("Position occupied. No moves made.");
            moves.removeLast();
            return null;
        }
        //start move
        String newBoard;
        boolean newEndgame = false;
        //Places game piece.
        char[] temp = this.board.toCharArray();
        temp[pos] = gamePiece();
        //Rotates given quadrant
        if(rotate == 'L') {
            newBoard = rotateLeft(quadrant,new String(temp));
        }
        else {
            newBoard = rotateRight(quadrant, new String(temp));
        }
        //Calculates heuristic values
        Point p = winner(newBoard);
        if(p.win != -1)
            newEndgame = true;
        int newHeuristicW = p.utilW * p.utilW;
        int newHeuristicB = p.utilB * p.utilB;
        if(newHeuristicW == 25) {
            newHeuristicW = 100;
        }
        if(newHeuristicB == 25) {
            newHeuristicB = 100;
        }
        return new Pentago(this, newBoard, move, newEndgame, p.win, newHeuristicW, newHeuristicB);
    }

    /**
     * Prints out game board in a formatted string.
     */
    public void print() {
        for(int i = 0; i < board.length(); i++) {
            if(i % 6 == 3)
                System.out.print(" |");
            if(i % 6 == 0) {
                if(i == 0)
                    System.out.println("+-------+-------+");
                else if(i == 18) {
                    System.out.println();
                    System.out.println("+-------+-------+");
                }
                else
                    System.out.println();
                System.out.print("|");
            }
            System.out.print(" " + board.charAt(i));
            if(i % 6 == 5)
                System.out.print(" |");
        }
        System.out.println();
        System.out.println("+-------+-------+");
    }

    //Determines winner of the given game board.
    private Point winner(String board) {
        int countW = 0;
        int countB = 0;
        for(int i = 0; i < board.length(); i++) {
            if(board.charAt(i) == 'w') {
                int temp = check(i, 'w', board);
                if(temp > countW)
                    countW = temp;
            }
            if(board.charAt(i) == 'b') {
                int temp = check(i, 'b', board);
                if(temp > countB)
                    countB = temp;
            }
        }
        return new Point(countW, countB, detWinner(countB, countW));
    }

    //Checks for game pieces in a row.
    private int check(int index, char ch, String board) {
        int max = 0;
        int count = 0;
        int i = 0;
        //Checks for pieces in a row
        do {
            if(board.charAt(index + i) == ch) {
                count++;
                i++;
            }
            else
                break;
        } while(index + i < board.length() && (index + i) % 6 != 0);
        if(count > max)
            max = count;
        count = 0;
        i = 0;
        //Checks for pieces in a forwards diagonal
        do {
            if(board.charAt(index + i) == ch) {
                count++;
                i += 5;
            }
            else
                break;
        } while(index + i < board.length() && (index + i) % 6 != 0);
        if(count > max)
            max = count;
        count = 0;
        i = 0;
        //Checks for pieces in a column
        for(int j = 0; index + j < board.length(); j += 6) {
            if(board.charAt(index + j) == ch)
                count++;
            else
                break;
        }
        if(count > max)
            max = count;
        count = 0;
        //Checks for pieces in a backwards diagonal
        do {
            if(board.charAt(index + i) == ch) {
                count++;
                i += 7;
            }
            else
                break;
        } while(index + i < board.length() && (index + i) % 6 != 0);
        if(count > max)
            max = count;
        return max;
    }

    //Determines flag for winner.
    private int detWinner(int countB, int countW) {
        if(countW >= 5 && countB >= 5)
            return 0;
        if(countB >= 5 && countW < countB)
            return 2;
        if(countW >= 5 && countB < countW)
            return 1;
        return -1;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        Pentago p = (Pentago)obj;
        if(((Pentago)obj).board.equals(this.board))
            return true;
        if(this.board.equals(p.board))
            return true;
        return false;
    }

    //Private class to hold heuristic values.
    private class Point {
        int utilW;
        int utilB;
        int win;

        public Point(int a, int b, int c) {
            utilW = a;
            utilB = b;
            win = c;
        }
    }

}