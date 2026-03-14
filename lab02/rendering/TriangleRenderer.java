package rendering;

import shapes.Triangle;
import shapes.Shape;
import java.awt.Graphics2D;

public class TriangleRenderer implements ShapeRenderer {
    @Override
    public void render(Graphics2D g, Shape shape) {
        Triangle tri = (Triangle) shape;
        g.drawPolygon(tri.getXPoints(), tri.getYPoints(), 3);
    }
}

// Note: Square and Circle reuse the renderers of their parents.
// No separate renderer classes needed for them, but they must be registered
// with the same renderers as Rectangle and Ellipse respectively.
