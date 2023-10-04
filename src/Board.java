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

    public boolean validateMove(Position currentPosition, Position newPosition) {
        Piece piece = getPiece(currentPosition);

        if (Colour.BLACK.equals(piece.getColour())) {
            return Math.abs(currentPosition.getColumn() - newPosition.getColumn()) == 1 && currentPosition.getRow() - newPosition.getRow() == 1 &&
                    getPiece(newPosition) == null;
        } else {
            return Math.abs(currentPosition.getColumn() - newPosition.getColumn()) == 1 && newPosition.getRow() - currentPosition.getRow() == 1 &&
                    getPiece(newPosition) == null;
        }
    }

    public boolean validateAttack(Position currentPosition, Position newPosition) {
        if (Math.abs(currentPosition.getRow() - newPosition.getRow()) == 2 && Math.abs(currentPosition.getColumn() - newPosition.getColumn()) == 2 && getPiece(newPosition) == null) {
            int rowDifference = newPosition.getRow() - currentPosition.getRow();
            int columnDifference = newPosition.getColumn() - currentPosition.getColumn();

            Position position = new Position(currentPosition.getRow() + rowDifference / 2, currentPosition.getColumn() + columnDifference / 2);

            return getPiece(currentPosition).getColour().equals(getPiece(position).getColour());
        }

        return false;
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
    }

//        System.out.println();
//        for (int i = 0; i < 8; i++) {
//            System.out.println(Arrays.toString(positions[i]));
//        }
//        System.out.println();
}
