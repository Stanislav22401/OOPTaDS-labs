package shapes;

/**
 * Abstract base class for all graphical shapes.
 * Shapes contain only geometric data; rendering is handled separately.
 */
public abstract class Shape {
    /**
     * Returns a short human-readable label for list views.
     */
    public abstract String getDisplayName();
}
