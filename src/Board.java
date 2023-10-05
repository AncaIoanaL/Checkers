import java.util.Arrays;

public class Board {

    private final Piece[][] positions = new Piece[8][8];

    public void initialiseBoard() {
        initialiseWhite();
        initialiseBlack();
    }

    private void initialiseWhite() {
        for (int i = 0; i < 3; i++) {
            if (i % 2 == 0) {
                for (int j = 1; j < 8; j = j + 2) {
                    positions[i][j] = new Piece(Colour.WHITE, new Position(i, j));
                }
            } else {
                for (int j = 0; j < 8; j = j + 2) {
                    positions[i][j] = new Piece(Colour.WHITE, new Position(i, j));
                }
            }
        }
    }

    private void initialiseBlack() {
        for (int i = 5; i < 8; i++) {
            if (i % 2 == 0) {
                for (int j = 1; j < 8; j = j + 2) {
                    positions[i][j] = new Piece(Colour.BLACK, new Position(i, j));
                }
            } else {
                for (int j = 0; j < 8; j = j + 2) {
                    positions[i][j] = new Piece(Colour.BLACK, new Position(i, j));
                }
            }
        }
    }

    public Piece getPiece(Position position) {
        return positions[position.getRow()][position.getColumn()];
    }

    public Piece getPiece(int row, int column) {
        return positions[row][column];
    }

    public Piece getPiece(Position position, int rowOffset, int columnOffset) {
        return positions[position.getRow() + rowOffset][position.getColumn() + columnOffset];
    }

    public boolean validateMove(Position currentPosition, Position newPosition) {
        Piece piece = getPiece(currentPosition);

        if (piece instanceof King) {
            return Math.abs(currentPosition.getColumn() - newPosition.getColumn()) == currentPosition.getRow() - newPosition.getRow() &&
                    getPiece(newPosition) == null;
        } else {
            if (Colour.BLACK.equals(piece.getColour())) {
                return currentPosition.getRow() - newPosition.getRow() == 1 && Math.abs(currentPosition.getColumn() - newPosition.getColumn()) == 1 &&
                        getPiece(newPosition) == null;
            } else {
                return newPosition.getRow() - currentPosition.getRow() == 1 && Math.abs(currentPosition.getColumn() - newPosition.getColumn()) == 1 &&
                        getPiece(newPosition) == null;
            }
        }
    }

    public boolean validateAttack(Position currentPosition, Position newPosition, Board board) {
        Piece piece = getPiece(currentPosition);

        if (piece instanceof King) {
            if (Math.abs(currentPosition.getRow() - newPosition.getRow()) == Math.abs(currentPosition.getColumn() - newPosition.getColumn()) &&
                    getPiece(newPosition) == null && validateBishopInBetweenPositions(currentPosition, newPosition, board)) {
                Piece attackPiece = board.getPiece(newPosition, 1, -1);
                Piece attackPiece2 =  board.getPiece(newPosition, -1, -1);
                Piece attackPiece3 = board.getPiece(newPosition, -1, 1);
                Piece attackPiece4 = board.getPiece(newPosition, 1, 1);

                return getPiece(currentPosition).getColour().equals(attackPiece.getColour()) || getPiece(currentPosition).getColour().equals(attackPiece2.getColour())
                        || getPiece(currentPosition).getColour().equals(attackPiece3.getColour()) || getPiece(currentPosition).getColour().equals(attackPiece4.getColour());
            }
        } else {
            if (Math.abs(currentPosition.getRow() - newPosition.getRow()) == 2 && Math.abs(currentPosition.getColumn() - newPosition.getColumn()) == 2 && getPiece(newPosition) == null) {
                int rowDifference = newPosition.getRow() - currentPosition.getRow();
                int columnDifference = newPosition.getColumn() - currentPosition.getColumn();

                Position position = new Position(currentPosition.getRow() + rowDifference / 2, currentPosition.getColumn() + columnDifference / 2);

                return getPiece(currentPosition).getColour().equals(getPiece(position).getColour());
            }
        }
        return false;
    }

    private boolean validateBishopInBetweenPositions(Position currentPosition, Position newPosition, Board board) {
        for (int i = 1; i <= Math.abs(currentPosition.getColumn() - newPosition.getColumn()); i++) {
            if (currentPosition.getRow() > newPosition.getRow() && currentPosition.getColumn() > newPosition.getColumn()) {
                if (board.getPiece(currentPosition, -i, -i) != null) {
                    return false;
                }
            } else if (currentPosition.getRow() > newPosition.getRow() && currentPosition.getColumn() < newPosition.getColumn()) {
                if (board.getPiece(currentPosition, -i, i) != null) {
                    return false;
                }
            } else if (currentPosition.getRow() < newPosition.getRow() && currentPosition.getColumn() < newPosition.getColumn()) {
                if (board.getPiece(currentPosition, i, i) != null) {
                    return false;
                }
            } else if (currentPosition.getRow() < newPosition.getRow() && currentPosition.getColumn() > newPosition.getColumn()) {
                if (board.getPiece(currentPosition, i, -i) != null) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean validateIfPieceCanBeKing(Position currentPosition, Colour colour) {
        if (Colour.BLACK.equals(colour)) {
            for (int i = 0; i < 8; i++) {
                if (currentPosition.getRow() == 0 && currentPosition.getColumn() == i) {
                    return true;
                }
            }
        } else {
            for (int i = 0; i < 8; i++) {
                if (currentPosition.getRow() == 7 && currentPosition.getColumn() == i) {
                    return true;
                }
            }
        }
        return false;
    }

    public void promoteToKing(Position currentPosition, Colour colour) {
        positions[currentPosition.getRow()][currentPosition.getColumn()] = new King(colour, currentPosition);
    }

    public void move(Position currentPosition, Position newPosition) {
        positions[currentPosition.getRow()][currentPosition.getColumn()].move(newPosition);

        positions[newPosition.getRow()][newPosition.getColumn()] = positions[currentPosition.getRow()][currentPosition.getColumn()];
        positions[currentPosition.getRow()][currentPosition.getColumn()] = null;
    }

    public void printBoard() {
        System.out.println();
        for (int i = 0; i < 8; i++) {
            System.out.print((8 - i) + " | ");
            for (int j = 0; j < 8; j++) {
                if (getPiece(i, j) == null) {
                    if ((i + j) % 2 == 0) {
                        System.out.print("□ ");
                    } else {
                        System.out.print("■ ");
                    }
                } else {
                    System.out.print(getPiece(i, j) + " ");
                }
            }
            System.out.println();
        }
        System.out.println("   ------------------------");
        System.out.println("    A  B  C  D  E  F  G  H");
        System.out.println();
    }
}
