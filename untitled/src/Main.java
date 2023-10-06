import java.util.*;

public class Main {

    static int pruneCount; // Count the amount of times the tree is pruned
    static int lookAhead; // To help the Is-Cutoff function

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


        if (selectedPart.equals("c")) {
            System.out.print("Number of moves to look ahead (depth): ");
            lookAhead = Integer.parseInt(scan.nextLine());

            // setup game
            boolean play = true;
            Board boardToPlay = new Board(numRows, numCols, consecToWin);
            System.out.print("Who plays first? 1=human, 2=computer: ");
            int player = Integer.parseInt(scan.nextLine());
            System.out.println(boardToPlay.to2DString());
            List<Integer> minimaxInfoObject = alphaBetaHeuristicSearch(initialState, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, transpostionTable);

            while(play) {
                if (player == 1) {
                    while (boardToPlay.getGameState() == GameState.IN_PROGRESS) {
                        List<Integer> minimaxInfoPlay = transpostionTable.get(boardToPlay);
                        System.out.println("The board has : " + transpostionTable.size() + " states.");
                        System.out.println("Minimax value for this state: " + minimaxInfoPlay.get(0) + ", optimal move: " + minimaxInfoPlay.get(1));
                        System.out.println("It is " + boardToPlay.getPlayerToMoveNext() + " turn!");
                        int move;
                        if (boardToPlay.getPlayerToMoveNext() == Player.MAX) {
                            System.out.print("Enter move: ");
                            move = Integer.parseInt(scan.nextLine());
                        } else {
                            move = minimaxInfoPlay.get(1);
                        }
                        boardToPlay = boardToPlay.makeMove(move);
                        System.out.println(boardToPlay.to2DString());

                        List<Integer> test = new ArrayList<>();
                        test.add(0);
                        test.add(0);
                        State testState = new State(boardToPlay, test);
                        transpostionTable.clear();
                        test = alphaBetaHeuristicSearch(testState, Integer.MIN_VALUE, Integer.MAX_VALUE,0, transpostionTable);

                    }
                    System.out.println("The winner is " + boardToPlay.getWinner());
                }
                else {
                    while (boardToPlay.getGameState() == GameState.IN_PROGRESS) {
                        List<Integer> minimaxInfoPlay = transpostionTable.get(boardToPlay);
                        System.out.println("The board has : " + transpostionTable.size() + " states.");
                        System.out.println("Minimax value for this state: " + minimaxInfoPlay.get(0) + ", optimal move: " + minimaxInfoPlay.get(1));
                        System.out.println("It is " + boardToPlay.getPlayerToMoveNext() + " turn!");
                        int move;
                        if (boardToPlay.getPlayerToMoveNext() == Player.MAX) {
                            move = minimaxInfoPlay.get(1);
                            System.out.println("Computer chooses move: " + move);
                        } else {
                            System.out.print("Enter move: ");
                            move = Integer.parseInt(scan.nextLine());
                        }
                        boardToPlay = boardToPlay.makeMove(move);
                        System.out.println(boardToPlay.to2DString());

                        List<Integer> test = new ArrayList<>();
                        test.add(0);
                        test.add(0);
                        State testState = new State(boardToPlay, test);
                        // test is not used just need to do this to have the transpositionTable updated.
                        transpostionTable.clear();
                        test = alphaBetaHeuristicSearch(testState, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, transpostionTable);

                    }
                    System.out.println("The winner is " + boardToPlay.getWinner());
                }
                System.out.print("Play again?(y/n): ");
                String playCheck = scan.nextLine();
                play = playCheck.equals("y");
                System.out.print("Who plays fist? 1=human, 2=computer: ");
                player = Integer.parseInt(scan.nextLine());
                boardToPlay = new Board(numRows, numCols, consecToWin);
                minimaxInfoObject = alphaBetaHeuristicSearch(initialState, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, transpostionTable);
            }
        }
        else if (selectedPart.equals("a")) {
            List<Integer> minimaxInfoObject = partAMinimaxSearch(initialState, transpostionTable);
            System.out.println("Transposition table has " + transpostionTable.size() + " states.");
            if (minimaxInfoObject.get(0) == 0) {
                System.out.println("Neither player has a guaranteed win; game will end in a tie with perfect play on both sides.");
            } else if (minimaxInfoObject.get(0) < 0) {
                System.out.println("Second player has a guaranteed win with perfect play");
            } else {
                System.out.println("First player has a guaranteed win with perfect play");
            }

            if (debug) {
                System.out.println("Transposition table:");
                List<String> transTableArray = new ArrayList<>();
                // help to iterate over table: https://www.geeksforgeeks.org/iterate-map-java/
                for (Map.Entry<Board, List<Integer>> entry : transpostionTable.entrySet()) {
                    String entryString = entry.getKey().toString() + " -> " + "MinimaxInfo[value=" + entry.getValue().get(0) + ", action=" + entry.getValue().get(1) + "]";
                    transTableArray.add(entryString);
                }
                transTableArray.sort(String::compareToIgnoreCase); // what
                for (String i : transTableArray) {
                    System.out.println(i);
                }
            }


        }
        else if (selectedPart.equals("b")) {
            int alpha = Integer.MIN_VALUE;
            int beta = Integer.MAX_VALUE;
            pruneCount = 0;

            List<Integer> minimaxInfoObject = alphaBetaSearch(initialState, alpha, beta, transpostionTable);
            System.out.println("Transposition table has " + transpostionTable.size() + " states.");
            System.out.println("The tree was pruned " + pruneCount + " times.");
            if (minimaxInfoObject.get(0) == 0) {
                System.out.println("Neither player has a guaranteed win; game will end in a tie with perfect play on both sides.");
            } else if (minimaxInfoObject.get(0) < 0) {
                System.out.println("Second player has a guaranteed win with perfect play");
            } else {
                System.out.println("First player has a guaranteed win with perfect play");
            }

            if (debug) {
                System.out.println("Transposition table:");
                List<String> transTableArray = new ArrayList<>();
                // help to iterate over table: https://www.geeksforgeeks.org/iterate-map-java/
                for (Map.Entry<Board, List<Integer>> entry : transpostionTable.entrySet()) {
                    String entryString = entry.getKey().toString() + " -> " + "MinimaxInfo[value=" + entry.getValue().get(0) + ", action=" + entry.getValue().get(1) + "]";
                    transTableArray.add(entryString);
                }
                transTableArray.sort(String::compareToIgnoreCase);
                for (String i : transTableArray) {
                    System.out.println(i);
                }
            }
        }

        if(selectedPart.equals("a") || selectedPart.equals("b")){
            // Play game!
            boolean play = true;
            Board boardToPlay = new Board(numRows, numCols, consecToWin);
            System.out.print("Who plays fist? 1=human, 2=computer: ");
            int player = Integer.parseInt(scan.nextLine());
            System.out.println(boardToPlay.to2DString());

            while (play) {
                if (player == 1) {
                    while (boardToPlay.getGameState() == GameState.IN_PROGRESS) {
                        List<Integer> minimaxInfoPlay = transpostionTable.get(boardToPlay);
                        System.out.println("Minimax value for this state: " + minimaxInfoPlay.get(0) + ", optimal move: " + minimaxInfoPlay.get(1));
                        System.out.println("It is " + boardToPlay.getPlayerToMoveNext() + " turn!");
                        int move;
                        if (boardToPlay.getPlayerToMoveNext() == Player.MAX) {
                            System.out.print("Enter move: ");
                            move = Integer.parseInt(scan.nextLine());
                        } else {
                            move = minimaxInfoPlay.get(1);
                        }
                        boardToPlay = boardToPlay.makeMove(move);
                        System.out.println(boardToPlay.to2DString());
                        if ((move != minimaxInfoPlay.get(1)) && selectedPart.equals("b")) {
                            List<Integer> test = new ArrayList<>();
                            test.add(0);
                            test.add(0);
                            State testState = new State(boardToPlay, test);
                            test = alphaBetaSearch(testState, Integer.MIN_VALUE, Integer.MAX_VALUE, transpostionTable);
                        }
                    }
                    System.out.println("The winner is " + boardToPlay.getWinner());
                } else {
                    while (boardToPlay.getGameState() == GameState.IN_PROGRESS) {
                        List<Integer> minimaxInfoPlay = transpostionTable.get(boardToPlay);
                        System.out.println("Minimax value for this state: " + minimaxInfoPlay.get(0) + ", optimal move: " + minimaxInfoPlay.get(1));
                        System.out.println("It is " + boardToPlay.getPlayerToMoveNext() + " turn!");
                        int move;
                        if (boardToPlay.getPlayerToMoveNext() == Player.MAX) {
                            move = minimaxInfoPlay.get(1);
                            System.out.println("Computer chooses move: " + move);
                        } else {
                            System.out.print("Enter move: ");
                            move = Integer.parseInt(scan.nextLine());
                        }
                        boardToPlay = boardToPlay.makeMove(move);
                        System.out.println(boardToPlay.to2DString());
                        if ((move != minimaxInfoPlay.get(1)) && selectedPart.equals("b")) {
                            List<Integer> test = new ArrayList<>();
                            test.add(0);
                            test.add(0);
                            State testState = new State(boardToPlay, test);
                            // test is not used just need to do this to have the transpositionTable updated.
                            test = alphaBetaSearch(testState, Integer.MIN_VALUE, Integer.MAX_VALUE, transpostionTable);
                        }
                    }
                    System.out.println("The winner is " + boardToPlay.getWinner());
                }
                System.out.print("Play again?(y/n): ");
                String playCheck = scan.nextLine();
                play = playCheck.equals("y");
                System.out.print("Who plays fist? 1=human, 2=computer: ");
                player = Integer.parseInt(scan.nextLine());
                boardToPlay = new Board(numRows, numCols, consecToWin);
                minimaxInfo = alphaBetaHeuristicSearch(initialState, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, transpostionTable);
            }
        }


    }

    // Part A
    public static List<Integer> partAMinimaxSearch(State state, Map<Board, List<Integer>> transpositionTable) {
        if (transpositionTable.containsKey(state.getBoard())){
            return transpositionTable.get(state.getBoard());
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
            transpositionTable.put(state.getBoard(), minimaxInfoUpdated);
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
            transpositionTable.put(state.getBoard(), minimaxInfoUpdated);
            return minimaxInfoUpdated;
        }

    }

    // Part B
    public static List<Integer> alphaBetaSearch(State state, int alpha, int beta, Map<Board, List<Integer>> transpositionTable){
        if (transpositionTable.containsKey(state.getBoard())){
            return transpositionTable.get(state.getBoard());
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
            List<Integer> minimaxInfoUpdated = new ArrayList<>();
            for (int action : actions(state)) {
                State child_state = result(state, action);
                List<Integer> childInfo = alphaBetaSearch(child_state, alpha, beta, transpositionTable);
                int v2 = childInfo.get(0);
                if (v2 > v){
                    v = v2;
                    bestMove = action;
                    alpha = Math.max(alpha, v);
                }
                if (v >= beta){
                    minimaxInfoUpdated.add(v);
                    minimaxInfoUpdated.add(bestMove);
                    pruneCount++;
                    return minimaxInfoUpdated;
                }
            }
            minimaxInfoUpdated.add(v);
            minimaxInfoUpdated.add(bestMove);
            transpositionTable.put(state.getBoard(), minimaxInfoUpdated);
            return minimaxInfoUpdated;
        }
        else {
            int v = Integer.MAX_VALUE;
            Integer best_move = null;
            List<Integer> minimaxInfoUpdated = new ArrayList<>();
            for (int action : actions(state)){
                State child_state = result(state, action);
                List<Integer> childInfo = alphaBetaSearch(child_state, alpha, beta, transpositionTable);
                int v2 = childInfo.get(0);
                if (v2 < v) {
                    v = v2;
                    best_move = action;
                    beta = Math.min(beta, v);
                }
                if (v <= alpha) {
                    minimaxInfoUpdated.add(v);
                    minimaxInfoUpdated.add(best_move);
                    pruneCount++;
                    return minimaxInfoUpdated;
                }
            }
            minimaxInfoUpdated.add(v);
            minimaxInfoUpdated.add(best_move);
            transpositionTable.put(state.getBoard(), minimaxInfoUpdated);
            return minimaxInfoUpdated;
        }
    }

    public static List<Integer> alphaBetaHeuristicSearch(State state, int alpha, int beta, int depth, Map<Board, List<Integer>> transpositionTable){
        if (transpositionTable.containsKey(state.getBoard())){
            return transpositionTable.get(state.getBoard());
        }
        else if (state.getBoard().getGameState() != GameState.IN_PROGRESS){
            int util = utility(state);
            List<Integer> minimaxInfoUpdated = new ArrayList<>();
            minimaxInfoUpdated.add(util);
            minimaxInfoUpdated.add(null);
            transpositionTable.put(state.getBoard(), minimaxInfoUpdated);
            return minimaxInfoUpdated;
        }
        else if (isCutoff(state, depth)) {
            int heuristic = eval(state);
            List<Integer> miniMaxInfoH = new ArrayList<>();
            miniMaxInfoH.add(heuristic);
            miniMaxInfoH.add(null);
            transpositionTable.put(state.getBoard(), miniMaxInfoH);
            return miniMaxInfoH;
        }
        else if (state.getToMove() == Player.MAX){
            int v = Integer.MIN_VALUE;
            Integer bestMove = null;
            List<Integer> minimaxInfoUpdated = new ArrayList<>();
            for (int action : actions(state)) {
                State child_state = result(state, action);
                List<Integer> childInfo = alphaBetaHeuristicSearch(child_state, alpha, beta, depth+1, transpositionTable);
                int v2 = childInfo.get(0);
                if (v2 > v){
                    v = v2;
                    bestMove = action;
                    alpha = Math.max(alpha, v);
                }
                if (v >= beta){
                    minimaxInfoUpdated.add(v);
                    minimaxInfoUpdated.add(bestMove);
                    pruneCount++;
                    return minimaxInfoUpdated;
                }
            }
            minimaxInfoUpdated.add(v);
            minimaxInfoUpdated.add(bestMove);
            transpositionTable.put(state.getBoard(), minimaxInfoUpdated);
            return minimaxInfoUpdated;
        }
        else {
            int v = Integer.MAX_VALUE;
            Integer best_move = null;
            List<Integer> minimaxInfoUpdated = new ArrayList<>();
            for (int action : actions(state)){
                State child_state = result(state, action);
                List<Integer> childInfo = alphaBetaHeuristicSearch(child_state, alpha, beta, depth+1, transpositionTable);
                int v2 = childInfo.get(0);
                if (v2 < v) {
                    v = v2;
                    best_move = action;
                    beta = Math.min(beta, v);
                }
                if (v <= alpha) {
                    minimaxInfoUpdated.add(v);
                    minimaxInfoUpdated.add(best_move);
                    pruneCount++;
                    return minimaxInfoUpdated;
                }
            }
            minimaxInfoUpdated.add(v);
            minimaxInfoUpdated.add(best_move);
            transpositionTable.put(state.getBoard(), minimaxInfoUpdated);
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
     * @param state given a state that is at the cutoff depth
     * @return its utility value which is associated with the chance that this state is likely to lead to a win.
     */
    public static int eval(State state) {
        Board board = state.getBoard();
        byte[][] boardAsByte = board.getBoardAsByte();
        int numRows = board.getRows();
        int numCols = board.getCols();

        int evalCount = 0;

        // check for 2 "wins" if so award 3 points to count depending on whose turn it is.
        for (int consecToWin = 2; consecToWin < 4; consecToWin++) {
            for (int r = 0; r < numRows; r++) {
                for (int c = 0; c < numCols; c++) {
                    if (boardAsByte[r][c] == 0) {
                        continue;
                    }

                    if ((c <= numCols - consecToWin && allMatchInARow(r, c, consecToWin, boardAsByte))
                    || (r <= numRows - consecToWin && allMatchInAColumn(r, c, consecToWin, boardAsByte))
                    || (r <= numRows - consecToWin && c <= numCols - consecToWin && allMatchInANorthEastDiagonal(r, c, consecToWin, boardAsByte))
                    || (r <= numRows - consecToWin && c - consecToWin >= -1 && allMatchInANorthWestDiagonal(r, c, consecToWin, boardAsByte))) {
                        if (boardAsByte[r][c] == 1) {
                            if (consecToWin == 2){
                                evalCount += 3;
                            }else {evalCount += 10;}
                        } else if (boardAsByte[r][c] == -1) {
                            if (consecToWin == 2){
                                evalCount -= 3;
                            }else{evalCount -= 10;}
                        }
                    }
                }
            }
        }

        return evalCount;
    }

    public static boolean isCutoff(State state, int depth){
        return depth >= lookAhead; // todo
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

    public static boolean allMatchInARow(int row, int startcol, int consecToWin, byte[][] board) {
        for (int x = 0; x < consecToWin - 1; x++) {
            if (board[row][startcol + x] != board[row][startcol + x + 1])
                return false;
        }
        return true;
    }

    public static boolean allMatchInAColumn(int startrow, int col, int consecToWin, byte[][] board){
        for (int x = 0; x < consecToWin - 1; x++) {
            if (board[startrow + x][col] != board[startrow + x + 1][col])
                return false;
        }
        return true;
    }

    public static boolean allMatchInANorthEastDiagonal(int startrow, int startcol, int consecToWin, byte[][] board){
        for (int x = 0; x < consecToWin - 1; x++) {
            if (board[startrow + x][startcol + x] != board[startrow + x + 1][startcol + x + 1])
                return false;
        }
        return true;
    }

    public static boolean allMatchInANorthWestDiagonal(int startrow, int startcol, int consecToWin, byte[][] board){
        for (int x = 0; x < consecToWin - 1; x++) {
            if (board[startrow + x][startcol - x] != board[startrow + x + 1][startcol - x - 1])
                return false;
        }
        return true;
    }

}
