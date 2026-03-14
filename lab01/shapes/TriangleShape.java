package shapes;

import java.awt.Graphics;
import java.awt.Polygon;

public class TriangleShape extends Shape {
    private int[] xPoints;
    private int[] yPoints;

    public TriangleShape(int x1, int y1, int x2, int y2, int x3, int y3) {
        xPoints = new int[]{x1, x2, x3};
        yPoints = new int[]{y1, y2, y3};
    }

    @Override
    public void draw(Graphics g) {
        g.drawPolygon(xPoints, yPoints, 3);
    }
}
