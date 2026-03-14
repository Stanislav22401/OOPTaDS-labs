package shapes;

/**
 * Represents a square (special rectangle with equal sides).
 */
public class Square extends Rectangle {
    public Square(int x, int y, int side) {
        super(x, y, side, side);
    }
}
