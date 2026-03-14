package drawing;

import shapes.Shape;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class ShapeList {
    private List<Shape> shapes;

    public ShapeList() {
        shapes = new ArrayList<>();
    }

    public void addShape(Shape shape) {
        shapes.add(shape);
    }

    public void drawAll(Graphics g) {
        for (Shape shape : shapes) {
            shape.draw(g);
        }
    }
}
