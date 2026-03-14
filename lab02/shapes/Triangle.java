package shapes;

import java.awt.Point;

/**
 * Represents a triangle defined by three vertices.
 */
public class Triangle extends Shape {
    private final int[] xPoints;
    private final int[] yPoints;

    public Triangle(Point p1, Point p2, Point p3) {
        xPoints = new int[]{p1.x, p2.x, p3.x};
        yPoints = new int[]{p1.y, p2.y, p3.y};
    }

    public int[] getXPoints() { return xPoints.clone(); }
    public int[] getYPoints() { return yPoints.clone(); }
}
