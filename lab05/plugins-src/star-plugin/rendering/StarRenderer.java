package rendering;

import shapes.Shape;
import shapes.Star;

import java.awt.Graphics2D;

/** Renders {@link Star} shapes from the star plugin. */
public class StarRenderer implements ShapeRenderer {
    @Override
    public void render(Graphics2D g, Shape shape) {
        Star star = (Star) shape;
        g.drawPolygon(star.getXPoints(), star.getYPoints(), star.getXPoints().length);
    }
}
