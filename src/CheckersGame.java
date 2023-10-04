import java.util.Scanner;

public class CheckersGame {

    private static final Player PLAYER_1 = new Player(Colour.BLACK, 1);
    private static final Player PLAYER_2 = new Player(Colour.WHITE, 2);

    public static void main(String[] args) {
        Board board = new Board();
        board.initialiseBoard();
        board.printBoard();
        Player currentPlayer = PLAYER_1;

        Scanner scanner = new Scanner(System.in);

        while (PLAYER_1.getPiecesCount() != 0 && PLAYER_2.getPiecesCount() != 0) {
            System.out.print("Player " + currentPlayer.getPlayerNumber() + " enter current position: ");
            Position currentPosition = readPosition(scanner);
            Piece pieceToMove = board.getPiece(currentPosition);

            System.out.print("Player " + currentPlayer.getPlayerNumber() + " enter new position: ");
            Position newPosition = readPosition(scanner);

            if (validatePlayerColour(pieceToMove, currentPlayer)) {
                if (board.validateMove(currentPosition, newPosition)) {
                    board.move(currentPosition, newPosition);
                    board.printBoard();

                    if (currentPlayer.equals(PLAYER_1)) {
                        currentPlayer = PLAYER_2;
                    } else {
                        currentPlayer = PLAYER_1;
                    }
                } else if (board.validateAttack(currentPosition, newPosition)) {
                    board.move(currentPosition, newPosition);
                    board.printBoard();

                    if (currentPlayer.equals(PLAYER_1)) {
                        PLAYER_2.decrementPieceCount();
                    } else {
                        PLAYER_1.decrementPieceCount();
                    }


                    // while (more attacks possible) {ask new position for attack and validate attack} - deja facuta sus in while


                }
            } else {
                System.out.println("Player " + currentPlayer.getPlayerNumber() + " this is an invalid move, please try again.");
            }

            if (currentPlayer.getPiecesCount() == 0) {
                System.out.println("Player " + currentPlayer.getPlayerNumber() + " HAS WON!");
            }
        }
    }

    private static Position readPosition(Scanner scanner) {
        String[] coordinates = scanner.nextLine().split(" ");
        int row = Integer.parseInt(coordinates[0]);
        int column = Integer.parseInt(coordinates[1]);
        return new Position(row, column);
    }

    private static boolean validatePlayerColour(Piece pieceToMove, Player currentPlayer) {
        return pieceToMove.getColour().equals(currentPlayer.getColour());
    }
}
