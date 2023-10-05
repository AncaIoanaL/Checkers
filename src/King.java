public class King extends Piece {

    public King(Colour colour, Position currentPosition) {
        super(colour, currentPosition);
    }

    @Override
    public void move(Position newPosition) {
        super.move(newPosition);
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
