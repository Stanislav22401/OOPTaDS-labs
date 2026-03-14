package drawing;

import shapes.Shape;
import java.util.ArrayList;
import java.util.List;

/**
 * Container for all shapes in the drawing.
 */
public class ShapeList {
    private List<Shape> shapes = new ArrayList<>();

    public void addShape(Shape shape) {
        shapes.add(shape);
    }

    public List<Shape> getShapes() {
        return shapes;
    }
}
