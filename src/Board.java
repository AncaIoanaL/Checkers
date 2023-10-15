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
            return Math.abs(currentPosition.getColumn() - newPosition.getColumn()) == Math.abs(currentPosition.getRow() - newPosition.getRow()) &&
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

    public boolean validateAttack(Position currentPosition, Position newPosition) {
        Piece currentPositionPiece = getPiece(currentPosition);
        Piece newPositionPiece = getPiece(newPosition);

        if (currentPositionPiece instanceof King) {
            if (Math.abs(currentPosition.getRow() - newPosition.getRow()) == Math.abs(currentPosition.getColumn() - newPosition.getColumn()) &&
                    newPositionPiece == null && validateInBetweenPositions(currentPosition, newPosition)) {
                int currentRow = currentPosition.getRow();
                int newRow = newPosition.getRow();
                int currentColumn = currentPosition.getColumn();
                int newColumn = newPosition.getColumn();

                Piece attackedPiece;

                // left down
                if (currentRow < newRow && currentColumn > newColumn) {
                    attackedPiece = getPiece(newPosition, -1, 1);
                // left up
                } else if (currentRow > newRow && currentColumn > newColumn) {
                    attackedPiece = getPiece(newPosition, 1, 1);
                // right up
                } else if (currentRow > newRow && currentColumn < newColumn) {
                    attackedPiece = getPiece(newPosition, 1, -1);
                // right down
                } else {
                    attackedPiece = getPiece(newPosition, -1, -1);
                }

                return attackedPiece != null && !currentPositionPiece.getColour().equals(attackedPiece.getColour());
            }
        } else {
            int rowDifference = newPosition.getRow() - currentPosition.getRow();
            int columnDifference = newPosition.getColumn() - currentPosition.getColumn();

            if (Math.abs(rowDifference) == 2 && Math.abs(columnDifference) == 2 && newPositionPiece == null) {
                return !currentPositionPiece.getColour().equals(getPiece(currentPosition, rowDifference / 2, columnDifference / 2).getColour());
            }
        }
        return false;
    }

    private boolean validateInBetweenPositions(Position currentPosition, Position newPosition) {
        for (int i = 1; i < Math.abs(currentPosition.getColumn() - newPosition.getColumn()) - 1; i++) {
            // left up
            if (currentPosition.getRow() > newPosition.getRow() && currentPosition.getColumn() > newPosition.getColumn()) {
                if (getPiece(currentPosition, -i, -i) != null) {
                    return false;
                }
            // right up
            } else if (currentPosition.getRow() > newPosition.getRow() && currentPosition.getColumn() < newPosition.getColumn()) {
                if (getPiece(currentPosition, -i, i) != null) {
                    return false;
                }
            // right down
            } else if (currentPosition.getRow() < newPosition.getRow() && currentPosition.getColumn() < newPosition.getColumn()) {
                if (getPiece(currentPosition, i, i) != null) {
                    return false;
                }
            // left down
            } else if (currentPosition.getRow() < newPosition.getRow() && currentPosition.getColumn() > newPosition.getColumn()) {
                if (getPiece(currentPosition, i, -i) != null) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean validateIfPieceCanBecomeKing(Position currentPosition, Colour colour) {
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

    public void attackMove(Position currentPosition, Position newPosition) {
        move(currentPosition, newPosition);

        int currentRow = currentPosition.getRow();
        int newRow = newPosition.getRow();
        int currentColumn = currentPosition.getColumn();
        int newColumn = newPosition.getColumn();

        Position attackedPiece;

        // left down
        if (currentRow < newRow && currentColumn > newColumn) {
            attackedPiece = getPiece(newPosition, -1, 1).getCurrentPosition();
        // left up
        } else if (currentRow > newRow && currentColumn > newColumn) {
            attackedPiece = getPiece(newPosition, 1, 1).getCurrentPosition();
        // right up
        } else if (currentRow > newRow && currentColumn < newColumn) {
            attackedPiece = getPiece(newPosition, 1, -1).getCurrentPosition();
        // right down
        } else {
            attackedPiece = getPiece(newPosition, -1, -1).getCurrentPosition();
        }

        positions[attackedPiece.getRow()][attackedPiece.getColumn()] = null;
    }

    public void printBoard() {
        for (int i = 0; i < 8; i++) {
            System.out.print("\n" + (8 - i) + " | ");
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
        }
        System.out.println("\n   ------------------------");
        System.out.println("    A  B  C  D  E  F  G  H");
        System.out.println();
    }
}
