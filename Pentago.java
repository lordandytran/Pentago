import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class Pentago {

    private char[] board;
    private LinkedList<String> moves;
    private int winner;
    private boolean endGame;

    public Pentago() {
        board = new char[36];
        for(int i = 0; i < 36; i++) {
            board[i] = '.';
        }
        moves = new LinkedList<String>();
        winner = -1;
        endGame = false;
    }

    private Pentago(Pentago p1, char[] newBoard, String newMove, boolean newEndgame, int newWinner) {
        board = newBoard;
        //Casting with abandon
        moves = (LinkedList<String>)p1.moves.clone();
        p1.getMoves().removeLast();
        endGame = newEndgame;
        winner = newWinner;
    }

    public char[] getBoard() {
        return board;
    }

    public LinkedList<String> getMoves() {
        return moves;
    }

    public int getWinner() {
        return winner;
    }

    public boolean isEndGame() {
        return endGame;
    }

    public char gamePiece() {
        if(moves.size() % 2 == 0)
            return 'b';
        else
            return 'w';
    }

    //Rotates the game board left at the given quadrant.
    private void rotateLeft(int offset, char temp[]) {
        char[] board = Arrays.copyOf(temp, 36);
        temp[0 + offset] = board[2 + offset];
        temp[1 + offset] = board[8 + offset];
        temp[2 + offset] = board[14 + offset];
        temp[6 + offset] = board[1 + offset];
        temp[7 + offset] = board[7 + offset];
        temp[8 + offset] = board[13 + offset];
        temp[12 + offset] = board[0 + offset];
        temp[13 + offset] = board[6 + offset];
        temp[14 + offset] = board[12 + offset];
    }

    //Rotates the game board right at the given quadrant.
    private void rotateRight(int offset, char[] temp) {
        char[] board = Arrays.copyOf(temp, 36);
        temp[0 + offset] = board[12 + offset];
        temp[1 + offset] = board[6 + offset];
        temp[2 + offset] = board[0 + offset];
        temp[6 + offset] = board[13 + offset];
        temp[7 + offset] = board[7 + offset];
        temp[8 + offset] = board[1 + offset];
        temp[12 + offset] = board[14 + offset];
        temp[13 + offset] = board[8 + offset];
        temp[14 + offset] = board[2 + offset];
    }

    public Pentago move(String move) {
        if(moves.size() == 36) {
            endGame = true;
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
        if(this.board[pos] != '.') {
            moves.removeLast();
            return null;
        }
        //Start move
        char[] newBoard;
        boolean newEndgame = false;
        //Places game piece.
        newBoard = Arrays.copyOf(this.board, 36);
        newBoard[pos] = gamePiece();

        //Rotates given quadrant
        int offset = 0;
        switch (quadrant) {
            case 1: break;
            case 2: offset = 3;
                break;
            case 3: offset = 18;
                break;
            case 4: offset = 21;
                break;
        }
        if(rotate == 'L') {
            rotateLeft(offset, newBoard);
        }
        else {
            rotateRight(offset, newBoard);
        }

        winner = checkWinner(newBoard);
        if(winner != -1)
            newEndgame = true;
        return new Pentago(this, newBoard, move, endGame, winner);
    }

    private int checkWinner(char[] newBoard) {
        int countW = 0;
        int countB = 0;
        for(int i = 0; i < newBoard.length; i++) {
            if(newBoard[i] == 'w') {
                int temp = check(i, 'w', newBoard);
                if(temp > countW)
                    countW = temp;
            }
            if(newBoard[i] == 'b') {
                int temp = check(i, 'b', newBoard);
                if(temp > countB)
                    countB = temp;
            }
        }
        return detWinner(countB,countW);
    }

    private int check(int index, char ch, char[] board) {
        int max = 0;
        int count = 0;
        int i = 0;
        //Checks for pieces in a row
        do {
            if(board[index + i] == ch) {
                count++;
                i++;
            }
            else
                break;
        } while(index + i < board.length && (index + i) % 6 != 0);
        if(count > max)
            max = count;
        count = 0;
        i = 0;
        //Checks for pieces in a forwards diagonal
        do {
            if(board[index + i] == ch) {
                count++;
                i += 5;
            }
            else
                break;
        } while(index + i < board.length && (index + i) % 6 != 0);
        if(count > max)
            max = count;
        count = 0;
        i = 0;
        //Checks for pieces in a column
        for(int j = 0; index + j < board.length; j += 6) {
            if(board[index + j] == ch)
                count++;
            else
                break;
        }
        if(count > max)
            max = count;
        count = 0;
        //Checks for pieces in a backwards diagonal
        do {
            if(board[index + i] == ch) {
                count++;
                i += 7;
            }
            else
                break;
        } while(index + i < board.length && (index + i) % 6 != 0);
        if(count > max)
            max = count;
        return max;
    }

    private int detWinner(int countB, int countW) {
        if(countW >= 5 && countB >= 5)
            return 0;
        if(countB >= 5 && countW < countB)
            return 2;
        if(countW >= 5 && countB < countW)
            return 1;
        return -1;
    }

    public void print() {
        for(int i = 0; i < board.length; i++) {
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
            System.out.print(" " + board[i]);
            if(i % 6 == 5)
                System.out.print(" |");
        }
        System.out.println();
        System.out.println("+-------+-------+");
    }
}