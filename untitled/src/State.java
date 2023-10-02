import java.util.List;

/**
 * Class to store the state of the game
 * Stores the Board, and Minimax info.
 */
public class State {
    private final Board board;
    private final List<Integer> minimaxInfo;
    private Player toMove; // Should this be a player ?

    /**
     * Constructs a new state
     */
    public State (Board board, List<Integer> minimaxInfo){
        this.board = board;
        this.minimaxInfo = minimaxInfo;
    }

    /**
     * @return The Board of state as a 2D array.
     */
    public String getBoard() {
        return board.toString();
    }

    /**
     * @return List containing the minimax info of state
     */
    public List<Integer> getMinimaxInfo() {
        return minimaxInfo;
    }

    /**
     * @return minimaxInfo as a format string displaying the value and action of the state.
     */
    public String minimaxInfoToString() {
        return "MinimaxInfo[value=" + minimaxInfo.get(0) + ", action=" + minimaxInfo.get(1) + "]";
    }

    /**
     * @return The player who will move next.
     */
    public Player getToMove() {
        return board.getPlayerToMoveNext();
    }

    /**
     * @return the state as a formatted string
     */
    @Override
    public String toString() {
        return getBoard() + " -> " + minimaxInfoToString();
    }
}
