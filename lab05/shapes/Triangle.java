package shapes;

import java.awt.Point;
import java.util.Arrays;

/**
 * Represents a triangle defined by three vertices.
 */
public class Triangle extends Shape {
    private int[] xPoints;
    private int[] yPoints;

    public Triangle(Point p1, Point p2, Point p3) {
        xPoints = new int[]{p1.x, p2.x, p3.x};
        yPoints = new int[]{p1.y, p2.y, p3.y};
    }

    public Triangle(int[] xPoints, int[] yPoints) {
        if (xPoints.length != 3 || yPoints.length != 3) {
            throw new IllegalArgumentException("Triangle requires exactly three vertices");
        }
        this.xPoints = Arrays.copyOf(xPoints, 3);
        this.yPoints = Arrays.copyOf(yPoints, 3);
    }

    public int[] getXPoints() { return xPoints.clone(); }
    public int[] getYPoints() { return yPoints.clone(); }

    public void setXPoints(int[] xPoints) {
        if (xPoints.length != 3) {
            throw new IllegalArgumentException("Triangle requires exactly three x coordinates");
        }
        this.xPoints = Arrays.copyOf(xPoints, 3);
    }

    public void setYPoints(int[] yPoints) {
        if (yPoints.length != 3) {
            throw new IllegalArgumentException("Triangle requires exactly three y coordinates");
        }
        this.yPoints = Arrays.copyOf(yPoints, 3);
    }

    public void setVertex(int index, int x, int y) {
        xPoints[index] = x;
        yPoints[index] = y;
    }

    @Override
    public String getDisplayName() {
        return String.format("Triangle (%d,%d),(%d,%d),(%d,%d)",
                xPoints[0], yPoints[0], xPoints[1], yPoints[1], xPoints[2], yPoints[2]);
    }
}
