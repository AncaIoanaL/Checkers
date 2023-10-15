import java.util.Scanner;

public class CheckersGame {

    private static final Player PLAYER_1 = new Player(Colour.BLACK, 1);
    private static final Player PLAYER_2 = new Player(Colour.WHITE, 2);

    public static void main(String[] args) {
        Board board = new Board();
        board.initialiseBoard();
        board.printBoard();
        Player currentPlayer = PLAYER_1;
        boolean playerNeedsToAttack = false;

        Scanner scanner = new Scanner(System.in);

        while (PLAYER_1.getPiecesCount() != 0 && PLAYER_2.getPiecesCount() != 0) {
            System.out.print(currentPlayer + " enter current position: ");
            Position currentPosition = readPosition(scanner);
            Piece pieceToMove = board.getPiece(currentPosition);

            System.out.print(currentPlayer + " enter new position: ");
            Position newPosition = readPosition(scanner);

            if (validatePlayerColour(pieceToMove, currentPlayer)) {
                if (!playerNeedsToAttack && board.validateMove(currentPosition, newPosition)) {
                    board.move(currentPosition, newPosition);

                    if (board.validateIfPieceCanBecomeKing(currentPosition, currentPlayer.getColour())) {
                        board.promoteToKing(currentPosition, currentPlayer.getColour());
                    }

                    board.printBoard();

                    currentPlayer = getNextPlayer(currentPlayer);
                } else if (board.validateAttack(currentPosition, newPosition)) {
                    board.attack(currentPosition, newPosition);
                    board.printBoard();

                    decrementPieceCount(currentPlayer);

                    if (currentPlayer.getPiecesCount() == 0) {
                        announceWinner(currentPlayer);
                    } else if (pieceToMove.validateIfAnotherAttackIsPossible(board)) {
                        System.out.print("Another attack is possible, " + currentPlayer + ". Enter one more position: ");
                        playerNeedsToAttack = true;
                    } else {
                        currentPlayer = getNextPlayer(currentPlayer);
                        playerNeedsToAttack = false;
                    }
                } else {
                    System.out.println(currentPlayer + " this is an invalid move, please try again.");
                }
            } else {
                System.out.println(currentPlayer + " this is an invalid move, please try again.");
            }
        }
    }

    private static Position readPosition(Scanner scanner) {
        String[] coordinates = scanner.nextLine().split(" ");
        int row = 8 - Integer.parseInt(coordinates[0]);
        int column = coordinates[1].charAt(0) - 65;
        return new Position(row, column);
    }

    private static boolean validatePlayerColour(Piece pieceToMove, Player currentPlayer) {
        return pieceToMove.getColour().equals(currentPlayer.getColour());
    }

    public static Player getNextPlayer(Player currentPlayer) {
        if (currentPlayer.equals(PLAYER_1)) {
            return PLAYER_2;
        } else {
            return PLAYER_1;
        }
    }

    public static void decrementPieceCount(Player currentPlayer) {
        if (currentPlayer.equals(PLAYER_1)) {
            PLAYER_2.decrementPieceCount();
        } else {
            PLAYER_1.decrementPieceCount();
        }
    }

    public static void announceWinner(Player currentPlayer) {
        if (currentPlayer.getPlayerNumber() == 1) {
            System.out.println("PLAYER 2 HAS WON!");
        } else {
            System.out.println("PLAYER 1 HAS WON!");
        }
    }
}
