import java.util.LinkedList;
import java.util.Scanner;

public class Pentago {

    private String board;
    private LinkedList<String> moves;
    private boolean endgame;
    private int winner;

    public Pentago() {
        char[] temp = new char[36];
        for(int i = 0; i < temp.length; i++) {
            temp[i] = '.';
        }
        board = new String(temp);
        moves = new LinkedList<String>();

        endgame = false;
        winner = -1;
    }

    public LinkedList<String> getMoves() {
        return moves;
    }

    public int getWinner() {
        return winner;
    }

    public String getBoard() {
        return board;
    }

    public boolean isEndgame() {
        return endgame;
    }

    private void rotateLeft(int quadrant) {
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

        board = new String(temp);
    }

    private void rotateRight(int quadrant) {
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

        board = new String(temp);
    }

    private char gamePiece() {
        if(moves.size() % 2 == 0)
            return 'b';
        else
            return 'w';
    }

    public int move(String move) {
        System.out.println(move);
        if(moves.size() == 36) {
            endgame = true;
            return 0;
        }
        int board = 0;
        int position = 0;
        int quadrant = 0;
        char rotate = 0;
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
            return -2;
        }
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
        if(this.board.charAt(pos) != '.') {
            System.out.println(this.board.charAt(pos));
            System.out.println("Position occupied. No moves made.");
            moves.removeLast();
            return -2;
        }
        char[] temp = this.board.toCharArray();
        temp[pos] = gamePiece();
        this.board = new String(temp);

        if(rotate == 'L') {
            rotateLeft(quadrant);
        }
        else {
            rotateRight(quadrant);
        }

        winner = winner();
        if(winner != -1)
            endgame = true;
        return winner;
    }

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
        System.out.println("+-------+-------+\n\n");
    }

    private int winner() {
        int countW = 0;
        int countB = 0;
        for(int i = 0; i < board.length(); i++) {
            if(board.charAt(i) == 'w') {
                int temp = check(i, 'w');
                if(temp > countW)
                    countW = temp;
            }
            if(board.charAt(i) == 'b') {
                int temp = check(i, 'b');
                if(temp > countB)
                    countB = temp;
            }
        }
        return detWinner(countB, countW);
    }

    private int check(int index, char ch) {
        int max = 0;
        int count = 0;
        int i = 0;
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

        for(int j = 0; index + j < board.length(); j += 6) {
            if(board.charAt(index + j) == ch)
                count++;
            else
                break;
        }
        if(count > max)
            max = count;
        count = 0;

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

    private int detWinner(int countB, int countW) {
        if(countW >= 5 && countB >= 5)
            return 0;
        if(countB >= 5 && countW < countB)
            return 2;
        if(countW >= 5 && countB < countW)
            return 1;
        return -1;
    }
}