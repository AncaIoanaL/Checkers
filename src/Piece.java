public class Piece {

    private Position currentPosition;
    private final Colour colour;

    public Piece(Colour colour, Position currentPosition) {
        this.colour = colour;
        this.currentPosition = currentPosition;
    }

    public Position getCurrentPosition() {
        return currentPosition;
    }

    public Colour getColour() {
        return colour;
    }

    public void move(Position newPosition) {
        currentPosition = newPosition;
    }

    public boolean validateIfAnotherAttackIsPossible(Board board) {
        return validateAnotherAttackIsPossibleOnTrajectory(board, 1, -1, -1) ||
                validateAnotherAttackIsPossibleOnTrajectory(board, 1, -1, 1) ||
                validateAnotherAttackIsPossibleOnTrajectory(board, 1, 1, -1) ||
                validateAnotherAttackIsPossibleOnTrajectory(board, 1, 1, 1);
    }

    public boolean validateAnotherAttackIsPossibleOnTrajectory(Board board, int positionsToCheck, int rowOffset, int columnOffset) {
        for (int i = 1; i < positionsToCheck; i++) {
            if (validateOutOfBoundsAttack(i + rowOffset, i + columnOffset)) {
                return false;
            }

            Piece attackedPiece = board.getPiece(currentPosition, i * rowOffset, i * columnOffset);

            if (attackedPiece != null) {
                return !attackedPiece.getColour().equals(colour) && board.getPiece(currentPosition, (i + 1) * rowOffset, (i + 1) * columnOffset) == null;
            }
        }

        return false;
    }

    public boolean validateOutOfBoundsAttack(int rowOffset, int columnOffset) {
        return currentPosition.getRow() + rowOffset <= 0 || currentPosition.getRow() + rowOffset >= 7 ||
                currentPosition.getColumn() + columnOffset <= 0 || currentPosition.getColumn() + columnOffset >= 7;
    }

    @Override
    public String toString() {
        if (Colour.BLACK.equals(getColour())) {
            return "⛂";
        } else {
            return "⛀";
        }
    }
}
