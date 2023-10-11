public class Player {

    private int piecesCount = 12;
    private final Colour colour;
    private final int playerNumber;

    public Player(Colour colour, int playerNumber) {
        this.colour = colour;
        this.playerNumber = playerNumber;
    }

    public int getPiecesCount() {
        return piecesCount;
    }

    public void decrementPieceCount() {
        this.piecesCount--;
    }

    public Colour getColour() {
        return colour;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (piecesCount != player.piecesCount) return false;
        if (playerNumber != player.playerNumber) return false;
        return colour == player.colour;
    }

    @Override
    public int hashCode() {
        int result = piecesCount;
        result = 31 * result + (colour != null ? colour.hashCode() : 0);
        result = 31 * result + playerNumber;
        return result;
    }

    @Override
    public String toString() {
        return "Player " + playerNumber;
    }
}
