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
            List<Integer> minimaxInfoObject = partAMinimaxSearch(initialState, transpostionTable);
            System.out.println(transpostionTable.size());
        }
    }

    public static List<Integer> partAMinimaxSearch(State state, Map<Board, List<Integer>> transpositionTable) {
        if (transpositionTable.containsKey(state)){
            return transpositionTable.get(state);
        }
        else if (state.getBoard().getGameState() != GameState.IN_PROGRESS){
            int util = utility(state);
            List<Integer> minimaxInfoUpdated = new ArrayList<>();
            minimaxInfoUpdated.add(util);
            minimaxInfoUpdated.add(null);
            transpositionTable.put(state.getBoard(), minimaxInfoUpdated);
            return minimaxInfoUpdated;
        }
        else if (state.getToMove() == Player.MAX){
            int v = Integer.MIN_VALUE;
            Integer bestMove = null;
            for (int action : actions(state)) {
                State child_state = result(state, action);
                List<Integer> childInfo = partAMinimaxSearch(child_state, transpositionTable);
                int v2 = childInfo.get(0);
                if (v2 > v){
                    v = v2;
                    bestMove = action;
                }
            }
            List<Integer> minimaxInfoUpdated = new ArrayList<>();
            minimaxInfoUpdated.add(v);
            minimaxInfoUpdated.add(bestMove);
            return minimaxInfoUpdated;
        }
        else {
            int v = Integer.MAX_VALUE;
            Integer best_move = null;
            for (int action : actions(state)){
                State child_state = result(state, action);
                List<Integer> childInfo = partAMinimaxSearch(child_state, transpositionTable);
                int v2 = childInfo.get(0);
                if (v2 < v) {
                    v = v2;
                    best_move = action;
                }
            }
            List<Integer> minimaxInfoUpdated = new ArrayList<>();
            minimaxInfoUpdated.add(v);
            minimaxInfoUpdated.add(best_move);
            return minimaxInfoUpdated;
        }

    }

    /**
     * @param state
     * @return the integer utility value of a terminal state
     */
    public static int utility(State state) {
        Board board = state.getBoard();
        if (board.getGameState() == GameState.MAX_WIN){
            return (int) (10000.0 * board.getRows() * board.getCols() / board.getNumberOfMoves());
        }
        if (board.getGameState() == GameState.MIN_WIN) {
            return (int) -(10000.0 * board.getRows() * board.getCols() / board.getNumberOfMoves());

        }
        else {
            return 0;
        }
    }

    /**
     *
     * @param state parent state
     * @return a list of columns a move could be placed
     */
    public static List<Integer> actions (State state) {
        List<Integer> actionsToReturn = new ArrayList<>();
        Board board = state.getBoard();

        for (int i = 0; i < board.getCols(); i++){
            if (!board.isColumnFull(i)) {
                actionsToReturn.add(i);
            }
        }
        return actionsToReturn;
    }

    /**
     *
     * @param state parent state
     * @param col action given
     * @return child state of parent state and action given
     */
    public static State result(State state, int col) {
        Board currBoard = state.getBoard();
        Board newBoard =  currBoard.makeMove(col);

        List<Integer> minimaxInfo = new ArrayList<>();
        minimaxInfo.add(state.getMinimaxInfo().get(0));
        minimaxInfo.add(col);

        return new State(newBoard, minimaxInfo);

    }


}
