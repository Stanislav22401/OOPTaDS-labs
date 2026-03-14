package shapes;

/**
 * Represents a circle (special ellipse with equal axes).
 */
public class Circle extends Ellipse {
    public Circle(int x, int y, int diameter) {
        super(x, y, diameter, diameter);
    }
}
