package shapes;

/**
 * Represents a square (rectangle with equal sides).
 */
public class Square extends Rectangle {
    public Square(int x, int y, int side) {
        super(x, y, side, side);
    }

    public int getSide() { return getWidth(); }

    public void setSide(int side) {
        setWidth(side);
        setHeight(side);
    }

    @Override
    public String getDisplayName() {
        return String.format("Square (%d,%d) side=%d", getX(), getY(), getWidth());
    }
}
