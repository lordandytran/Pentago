import java.util.Random;
import java.util.Scanner;
import java.util.LinkedList;

public class Play {

    public static void main(String[] args) {

        //Player vs Computer minimax
        minimaxpvc();
        //Player vs Computer alpha-beta pruning
        alphabetapvc();
        //Computer vs Computer
        compvcomp(); //uses alpha-beta pruning


    }

    /************* Computer vs Computer **************/
    public static void compvcomp() {
        Pentago p1 = new Pentago();
        LinkedList<String> list1 = new LinkedList<String>();
        LinkedList<String> list2 = new LinkedList<String>();
        int index1, index2;
        int nodes = 0;
        p1 = p1.move(ai());
        System.out.println("Computer 1");
        System.out.println("Token: " + p1.gamePiece());
        System.out.println("Move: " + p1.getMoves().getLast());
        p1.print();
        System.out.println();

        while(!p1.isEndGame()) {
            MiniMax m2 = new MiniMax(2, p1);
            m2.alphabeta(m2.getCurrTree(), m2.getDepth(), Integer.MIN_VALUE, Integer.MAX_VALUE, true);
            list2 = m2.getListmove();
            index2 = list2.indexOf(p1.getMoves().getLast());
            index2++;
            p1 = p1.move(list2.get(index2));
            System.out.println("Computer 2");
            System.out.println("Token: " + p1.gamePiece());
            System.out.println("Move: " + p1.getMoves().getLast());
            p1.print();
            System.out.println();
            if(play(p1.getWinner()))
                break;

            MiniMax m1 = new MiniMax(2, p1);
            m1.alphabeta(m1.getCurrTree(), m1.getDepth(), Integer.MIN_VALUE, Integer.MAX_VALUE, true);
            list1 = m1.getListmove();
            index1 = list1.indexOf(p1.getMoves().getLast());
            index1++;
            p1 = p1.move(list1.get(index1));
            System.out.println("Computer 1");
            System.out.println("Token: " + p1.gamePiece());
            System.out.println("Move: " + p1.getMoves().getLast());
            p1.print();
            System.out.println();
            if(play(p1.getWinner()))
                break;
            //nodes += m1.nodes;
            //System.out.println(nodes);
        }
    }

    /************* MiniMax Player vs Computer **************/
    public static void minimaxpvc() {
        Pentago p1 = new Pentago();
        Scanner scan = new Scanner(System.in);

        System.out.print("Do you want to go first? (Y/N): ");
        LinkedList<String> list = new LinkedList<String>();
        int index;
        if(scan.nextLine().equals("N")) {
            //random first move by ai
            p1 = p1.move(ai());
            System.out.println("Computer");
            System.out.println("Token: " + p1.gamePiece());
            System.out.println("Move: " + p1.getMoves().getLast());
            p1.print();
            System.out.println();
        }
        int nodes = 0;
        MiniMax m = new MiniMax(2, p1);
        m.minimax(m.getCurrTree(), m.getDepth(), true);
        //list = m.getListmove();
        //nodes += m.nodes;
        System.out.println(nodes);

        while(!p1.isEndGame()) {
            System.out.print("Enter move: ");
            String in = scan.nextLine();
            while(p1.move(in) == null) {
                System.out.print("Invalid Move. Try Again: ");
                in = scan.nextLine();
            }
            System.out.println();
            p1 = p1.move(in);
            System.out.println("Player");
            System.out.println("Token: " + p1.gamePiece());
            System.out.println("Move: " + p1.getMoves().getLast());
            p1.print();
            System.out.println();
            if(play(p1.getWinner()))
                break;
            index = list.indexOf(p1.getMoves().getLast());
            //System.out.println(list);
            m = new MiniMax(1, p1);
            m.minimax(m.getCurrTree(), m.getDepth(), true);
            list = m.getListmove();
            index = list.indexOf(p1.getMoves().getLast());
            index++;
            p1 = p1.move(list.get(index));
            System.out.println("Computer");
            System.out.println("Token: " + p1.gamePiece());
            System.out.println("Move: " + p1.getMoves().getLast());
            p1.print();
            //System.out.println();
            //nodes += m.nodes;
            System.out.println(nodes);
            if(play(p1.getWinner()))
                break;
        }
    }

    /************* Alpha-Beta Pruning Player vs Computer **************/
    public static void alphabetapvc() {
        Pentago p1 = new Pentago();
        Scanner scan = new Scanner(System.in);

        System.out.print("Do you want to go first? (Y/N): ");
        LinkedList<String> list = new LinkedList<String>();
        int index;
        if(scan.nextLine().equals("N")) {
            //random first move by ai
            p1 = p1.move(ai());
            System.out.println("Computer");
            System.out.println("Token: " + p1.gamePiece());
            System.out.println("Move: " + p1.getMoves().getLast());
            p1.print();
            System.out.println();
        }

        MiniMax m = new MiniMax(2, p1);
        m.alphabeta(m.getCurrTree(), m.getDepth(), Integer.MIN_VALUE, Integer.MAX_VALUE, true);
        list = m.getListmove();


        while(!p1.isEndGame()) {
            System.out.print("Enter move: ");
            String in = scan.nextLine();
            while(p1.move(in) == null) {
                System.out.print("Invalid Move. Try Again: ");
                in = scan.nextLine();
            }
            System.out.println();
            p1 = p1.move(in);
            System.out.println("Player");
            System.out.println("Token: " + p1.gamePiece());
            System.out.println("Move: " + p1.getMoves().getLast());
            p1.print();
            System.out.println();
            if(play(p1.getWinner()))
                break;
            index = list.indexOf(p1.getMoves().getLast());
            //System.out.println(list);
            m = new MiniMax(2, p1);
            m.alphabeta(m.getCurrTree(), m.getDepth(), Integer.MIN_VALUE, Integer.MAX_VALUE, true);
            list = m.getListmove();
            index = list.indexOf(p1.getMoves().getLast());
            index++;
            p1 = p1.move(list.get(index));
            System.out.println("Computer");
            System.out.println("Token: " + p1.gamePiece());
            System.out.println("Move: " + p1.getMoves().getLast());
            p1.print();
            System.out.println();
            if(play(p1.getWinner()))
                break;
        }
    }



    private static boolean play(int winner) {
        boolean ret = false;
        if(winner == 1) {
            System.out.println("Player 1 wins.");
            ret = true;
        }
        if(winner == 2) {
            System.out.println("Player 2 wins.");
            ret = true;
        }
        if(winner == 0) {
            System.out.println("Tie game.");
            ret = true;
        }
        return ret;
    }

    private static String ai() {
        Random rand = new Random();
        String output = "";
        output += String.valueOf(rand.nextInt(4) + 1);
        output += "/";
        output += String.valueOf(rand.nextInt(9) + 1);
        output += " ";
        output += String.valueOf(rand.nextInt(4) + 1);
        if((rand.nextInt(9) + 1) % 2 == 0)
            output += "L";
        else
            output += "R";
        return output;
    }
}
