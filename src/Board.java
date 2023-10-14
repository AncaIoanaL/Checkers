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
        Piece currentPositionPiece = getPiece(currentPosition);
        Piece newPositionPiece = getPiece(newPosition);

        if (currentPositionPiece instanceof King) {
            if (Math.abs(currentPosition.getRow() - newPosition.getRow()) == Math.abs(currentPosition.getColumn() - newPosition.getColumn()) &&
                    newPositionPiece == null && validateInBetweenPositions(currentPosition, newPosition, board)) {
                int currentRow = currentPosition.getRow();
                int newRow = newPosition.getRow();
                int currentColumn = currentPosition.getColumn();
                int newColumn = newPosition.getColumn();

                Piece attackedPiece;

                if (currentRow < newRow && currentColumn > newColumn) {
                    attackedPiece = board.getPiece(newPosition, -1, 1);
                } else if (currentRow > newRow && currentColumn > newColumn) {
                    attackedPiece = board.getPiece(newPosition, 1, 1);
                } else if (currentRow > newRow && currentColumn < newColumn) {
                    attackedPiece = board.getPiece(newPosition, 1, -1);
                } else {
                    attackedPiece = board.getPiece(newPosition, -1, -1);
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

    private boolean validateInBetweenPositions(Position currentPosition, Position newPosition, Board board) {
        for (int i = 1; i < Math.abs(currentPosition.getColumn() - newPosition.getColumn()) - 1; i++) {
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

    public boolean validateIfAnotherAttackIsPossible(Position currentPosition, Board board) {
        Piece currentPositionPiece = getPiece(currentPosition);
        int row = currentPosition.getRow();
        int column = currentPosition.getColumn();


        if (!validateInBetweenPositions(currentPosition, new Position(row - Math.min(row, column), column - Math.min(row, column)), board)) {
            for ( int i = 1; i < Math.min(row, column); i ++) {
                if (!board.getPiece(currentPosition, -i, -i).getColour().equals(currentPositionPiece.getColour())) {
                    if (board.getPiece(currentPosition, -i - 1, -i - 1) == null) {
                        return true;
                    }
                }
            }// left up
        } else if (!validateInBetweenPositions(currentPosition, new Position(row - Math.min(row, 7 -  column), column + Math.min(row, 7 - column)), board)) {
            for ( int i = 1; i < Math.min(row, column); i ++) {
                if (!board.getPiece(currentPosition, -i, i).getColour().equals(currentPositionPiece.getColour())) {
                    if (board.getPiece(currentPosition, -i - 1, i + 1) == null) {
                        return true;
                    }
                }
            }//right up
        } else if (!validateInBetweenPositions(currentPosition, new Position(row + Math.min(7 - row, 7 - column), column + Math.min(7 - row, 7 - column)), board)) {
            for ( int i = 1; i < Math.min(row, column); i ++) {
                if (!board.getPiece(currentPosition, i, i).getColour().equals(currentPositionPiece.getColour())) {
                    if (board.getPiece(currentPosition, i + 1, i + 1) == null) {
                        return true;
                    }
                }
            }//right down
        } else if (!validateInBetweenPositions(currentPosition, new Position(row + Math.min(7 - row, column), column - Math.min(7 - row, column)), board)) {
            for ( int i = 1; i < Math.min(row, column); i ++) {
                if (!board.getPiece(currentPosition, i, -i).getColour().equals(currentPositionPiece.getColour())) {
                    if (board.getPiece(currentPosition, i + 1, -i - 1) == null) {
                        return true;
                    }
                }
            }
        }// left down

        return false;
    }
}
