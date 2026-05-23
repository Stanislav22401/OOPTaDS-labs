package drawing.observer;

import drawing.ShapeList;

/**
 * Observer pattern: notified when the {@link ShapeList} content changes.
 */
public interface ShapeListObserver {
    void onShapeListChanged(ShapeList source);
}
