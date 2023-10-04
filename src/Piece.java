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

    @Override
    public String toString() {
        if (Colour.BLACK.equals(getColour())) {
            return "⛂";
        } else {
            return "⛀";
        }
    }
}
