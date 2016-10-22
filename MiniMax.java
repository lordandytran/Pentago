import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class MiniMax {

    //Private tree class. Represents the game search tree.
    private class Tree {
        public Pentago curr;
        public ArrayList<Tree> successors;

        public Tree(Pentago state) {
            curr = state;
            successors = new ArrayList<Tree>();
        }
        //Generate successors generates all possible moves from the current state into an arraylist
        public void generateSucessors() {
            ArrayList<Pentago> arr = new ArrayList<Pentago>();
            HashSet<Pentago> hs = new HashSet<Pentago>();
            for(int i = 1; i <= 4; i++) {
                for(int j = 1; j <= 9; j++) {
                    for(int k = 1; k <= 4; k++) {
                        Pentago p1 = curr.move(i + "/" + j + " " + k + "L");
                        if(p1 != null && !hs.contains(p1)) {
                            hs.add(p1);
                        }
                        Pentago p2 = curr.move(i + "/" + j + " " + k + "R");
                        if(p2 != null && !hs.contains(p2)) {
                            hs.add(p2);
                        }
                    }
                }
            }
            Object[] temp = hs.toArray();
            arr = new ArrayList<Pentago>();
            for(int i = 0; i < temp.length; i++) {
                //casting with abandon
                arr.add((Pentago)temp[i]);
            }
            for(int i = 0; i < arr.size(); i++) {
                successors.add(new Tree(arr.get(i)));
            }
        }
    }

    //Current game state
    private Pentago curr;
    //used for minimax algorithm
    private int max;
    //Depth limit of search
    private int depth;
    //list of moves of best move
    private LinkedList<String> listmove;
    //game tree of current state
    private Tree currTree;

    public int nodes;

    public MiniMax(int newDepth, Pentago newNode) {
        nodes = 0;
        depth = newDepth;
        max = 0;
        currTree = new Tree(newNode);
        //listmove = new LinkedList<String>();
    }

    public Pentago getCurr() {
        return curr;
    }

    public Tree getCurrTree() {
        return currTree;
    }

    public LinkedList<String> getListmove() {
        return listmove;
    }

    public int getDepth() {
        return depth;
    }

    //Minimax Algorithm
    public int minimax(Tree currentTree, int depth, boolean player) {
        int val = 0;
        if(depth == 0) {
            listmove = currentTree.curr.getMoves();
            return currentTree.curr.getHeuristic();
        }
        if(player) {
            val = Integer.MIN_VALUE;
            currentTree.generateSucessors();
            ArrayList<Tree> suc = currentTree.successors;
            for(int i = 0; i < suc.size(); i++) {
                nodes++;
                int newVal = minimax(suc.get(i), depth - 1, false);
                if (val < newVal)
                    val = newVal;
            }
            if(val > max && depth == this.depth - 1) {
                max = val;
                curr = currentTree.curr;
            }
            return val;
        }
        else {
            val = Integer.MAX_VALUE;
            currentTree.generateSucessors();
            ArrayList<Tree> suc = currentTree.successors;
            for(int i = 0; i < suc.size(); i++) {
                nodes++;
                int newVal = minimax(suc.get(i), depth - 1, true);
                if (val > newVal)
                    val = newVal;
            }
            return val;
        }
    }

    //Alpha-Beta pruning algorithm
    public int alphabeta(Tree currentTree, int depth, int alpha, int beta, boolean player) {
        if (depth == 0) {
            listmove = currentTree.curr.getMoves();
            return currentTree.curr.getHeuristic();
        }
        if (player) {
            currentTree.generateSucessors();
            ArrayList<Tree> suc = currentTree.successors;
            for(int i = 0; i < suc.size(); i++) {
                nodes++;
                alpha = Math.max(alpha, alphabeta(suc.get(i), depth - 1, alpha, beta, false));
                if (beta < alpha) {
                    break;
                }
            }
            return alpha;
        }
        else {
            currentTree.generateSucessors();
            ArrayList<Tree> suc = currentTree.successors;
            for(int i = 0; i < suc.size(); i++) {
                alpha = Math.min(alpha, alphabeta(suc.get(i), depth - 1, alpha, beta, true));
                nodes++;
                if (beta < alpha) {
                    break;
                }
            }
            return beta;
        }
    }
}