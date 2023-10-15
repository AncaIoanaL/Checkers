public class King extends Piece {

    public King(Colour colour, Position currentPosition) {
        super(colour, currentPosition);
    }

    @Override
    public void move(Position newPosition) {
        super.move(newPosition);
    }

    @Override
    public boolean validateIfAnotherAttackIsPossible(Board board) {
        int column = getCurrentPosition().getColumn();
        int row = getCurrentPosition().getRow();

        return validateAnotherAttackIsPossibleOnTrajectory(board, Math.min(row, column), -1, 1) ||
                validateAnotherAttackIsPossibleOnTrajectory(board, Math.min(row, 7 - column), -1, 1) ||
                validateAnotherAttackIsPossibleOnTrajectory(board, Math.min(7 - row, column), 1, -1) ||
                validateAnotherAttackIsPossibleOnTrajectory(board, Math.min(7 - row, 7 - column), 1, 1);
    }

    @Override
    public String toString() {
        if (Colour.BLACK.equals(getColour())) {
            return "⛃";
        } else {
            return "⛁";
        }
    }
}
