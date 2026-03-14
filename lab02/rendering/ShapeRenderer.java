package rendering;

import shapes.Shape;
import java.awt.Graphics2D;

/**
 * Interface for rendering a specific shape type.
 */
public interface ShapeRenderer {
    void render(Graphics2D g, Shape shape);
}
