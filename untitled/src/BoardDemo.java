import java.util.Scanner;

public class BoardDemo {
    public static void main(String[] args) {
        Board board = new Board(6, 7, 4); // standard connect 4 size
        System.out.println("Here is the board:");
        System.out.println(board.to2DString());

        // MAX (X) makes a move:
        board = board.makeMove(3);
        System.out.println("Here is the updated board:");
        System.out.println(board.to2DString());

        // MIN (O) makes a move:
        board = board.makeMove(2);
        System.out.println("Here is the updated board:");
        System.out.println(board.to2DString());

        // Check the game progress:
        System.out.println("State of the game: " + board.getGameState());

        System.out.println("Play MAX and MIN on keyboard - MAX goes first");
        Scanner scan = new Scanner(System.in);
        System.out.print("Number of rows: ");
        int numRows = Integer.parseInt(scan.nextLine());
        System.out.print("Number of columns ");
        int numCols = Integer.parseInt(scan.nextLine());
        System.out.print("Consecutive to win: ");
        int consecToWin = Integer.parseInt(scan.nextLine());

        Board boardTest = new Board(numRows, numCols, consecToWin);
        System.out.println(boardTest.to2DString());
        while (boardTest.getGameState() == GameState.IN_PROGRESS){
            System.out.println("It is " + boardTest.getPlayerToMoveNext() + " move.");
            System.out.print("Enter column to place piece: ");
            int newCol = Integer.parseInt(scan.nextLine());
            boardTest = boardTest.makeMove(newCol);
            System.out.println(boardTest.to2DString());
        }
        System.out.println(boardTest.getWinner() + " wins!");


        /*
        // Demo with a smaller board:
        board = new Board(3, 3, 2); // This a rather silly game.
        System.out.println("Here is the board:");
        System.out.println(board.to2DString());

        // MAX (X) makes a move:
        board = board.makeMove(2);
        System.out.println("Here is the updated board:");
        System.out.println(board.to2DString());

        // MIN (O) makes a move:
        board = board.makeMove(1);
        System.out.println("Here is the updated board:");
        System.out.println(board.to2DString());

        // MAX (X) makes a move:
        board = board.makeMove(2);
        System.out.println("Here is the updated board:");
        System.out.println(board.to2DString());

        // Check the game progress:
        System.out.println("State of the game: " + board.getGameState());
         */
    }
}
