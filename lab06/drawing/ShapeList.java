package drawing;

import drawing.observer.ShapeListObserver;
import shapes.Shape;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Observable container for shapes (Observer pattern).
 */
public class ShapeList {
    private final List<Shape> shapes = new ArrayList<>();
    private final List<ShapeListObserver> observers = new ArrayList<>();

    public void addObserver(ShapeListObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers() {
        for (ShapeListObserver observer : observers) {
            observer.onShapeListChanged(this);
        }
    }

    public void addShape(Shape shape) {
        shapes.add(shape);
        notifyObservers();
    }

    public void removeAt(int index) {
        shapes.remove(index);
        notifyObservers();
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
        notifyObservers();
    }
}
