import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.print("Which part would you like to play(a,b,c): ");
        String selectedPart = scan.nextLine();
        System.out.print("Debugging info?(y/n): ");
        boolean debug = scan.nextLine().equals("y");
        System.out.print("Number of rows: ");
        int numRows = Integer.parseInt(scan.nextLine());
        System.out.print("Number of columns ");
        int numCols = Integer.parseInt(scan.nextLine());
        System.out.print("Consecutive to win: ");
        int consecToWin = Integer.parseInt(scan.nextLine());

        // Create Board, initial state, and transposition table
        Board board = new Board(numRows, numCols, consecToWin);
        List<Integer> minimaxInfo = new ArrayList<>();
        minimaxInfo.add(0);
        minimaxInfo.add(0);
        State initialState = new State(board, minimaxInfo);
        Map<Board, List<Integer>> transpostionTable = new HashMap<>();


        if (selectedPart.equals("a")){
            partAMinimaxSearch(initialState, transpostionTable, debug);
        }
    }

    public static void partAMinimaxSearch(State initialState, Map<Board, List<Integer>> transpositionTable, boolean debug) {
        // call works and can get into the function.
    }
}
