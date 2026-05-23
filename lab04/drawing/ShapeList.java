package drawing;

import shapes.Shape;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Container for all shapes in the drawing.
 */
public class ShapeList {
    private final List<Shape> shapes = new ArrayList<>();

    public void addShape(Shape shape) {
        shapes.add(shape);
    }

    public void removeAt(int index) {
        shapes.remove(index);
    }

    public Shape get(int index) {
        return shapes.get(index);
    }

    public List<Shape> getShapes() {
        return Collections.unmodifiableList(shapes);
    }

    public int size() {
        return shapes.size();
    }

    public void clear() {
        shapes.clear();
    }
}
