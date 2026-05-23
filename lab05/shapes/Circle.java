package shapes;

/**
 * Represents a circle (ellipse with equal axes).
 */
public class Circle extends Ellipse {
    public Circle(int x, int y, int diameter) {
        super(x, y, diameter, diameter);
    }

    public int getDiameter() { return getWidth(); }

    public void setDiameter(int diameter) {
        setWidth(diameter);
        setHeight(diameter);
    }

    @Override
    public String getDisplayName() {
        return String.format("Circle (%d,%d) d=%d", getX(), getY(), getWidth());
    }
}
