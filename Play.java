import java.util.Random;
import java.util.Scanner;

public class Play {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Pentago game = new Pentago();
        while(!game.isEndgame()) {
            System.out.print("Enter move: ");
            int i = play(game.move(in.nextLine()));
            while (i == -2)
                play(game.move(in.nextLine()));
            game.print();

            i = play(game.move(ai()));
            while (i == -2)
                play(game.move(ai()));
            game.print();
        }
    }

    private static int play(int winner) {
        //System.out.println(winner);
        if(winner == 1)
            System.out.println("Player 1 wins.");
        if(winner == 2)
            System.out.println("Player 2 wins.");
        if(winner == 0) {
            System.out.println("Tie game.");
        }
        return winner;
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