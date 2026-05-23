package rendering;

import shapes.Triangle;
import shapes.Shape;
import java.awt.Graphics2D;

/** Renders {@link Triangle} shapes. */
public class TriangleRenderer implements ShapeRenderer {
    @Override
    public void render(Graphics2D g, Shape shape) {
        Triangle tri = (Triangle) shape;
        g.drawPolygon(tri.getXPoints(), tri.getYPoints(), 3);
    }
}
