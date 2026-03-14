package rendering;

import shapes.Ellipse;
import shapes.Shape;
import java.awt.Graphics2D;

public class EllipseRenderer implements ShapeRenderer {
    @Override
    public void render(Graphics2D g, Shape shape) {
        Ellipse ell = (Ellipse) shape;
        g.drawOval(ell.getX(), ell.getY(), ell.getWidth(), ell.getHeight());
    }
}
