package rendering;

import shapes.Line;
import shapes.Shape;
import java.awt.Graphics2D;

public class LineRenderer implements ShapeRenderer {
    @Override
    public void render(Graphics2D g, Shape shape) {
        Line line = (Line) shape;
        g.drawLine(line.getX1(), line.getY1(), line.getX2(), line.getY2());
    }
}
