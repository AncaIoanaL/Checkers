public class Position {

    private final int row;
    private final int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Position(Position position, int rowOffset, int columnOffset) {
        this.row = position.getRow() + rowOffset;
        this.column = position.getColumn() + columnOffset;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return "row=" + row + ", column=" + column;
    }
}
