package rendering;

import shapes.Rectangle;
import shapes.Shape;
import java.awt.Graphics2D;

/** Renders {@link Rectangle} and subclasses that share the same drawing logic. */
public class RectangleRenderer implements ShapeRenderer {
    @Override
    public void render(Graphics2D g, Shape shape) {
        Rectangle rect = (Rectangle) shape;
        g.drawRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }
}
