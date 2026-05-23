package rendering;

import shapes.Shape;
import java.util.HashMap;
import java.util.Map;

/**
 * Maps shape classes to renderers. Lookup walks the inheritance chain
 * so subclasses can reuse parent renderers without duplicate registration.
 */
public class RendererRegistry {
    private final Map<Class<? extends Shape>, ShapeRenderer> renderers = new HashMap<>();

    public void register(Class<? extends Shape> shapeClass, ShapeRenderer renderer) {
        renderers.put(shapeClass, renderer);
    }

    /**
     * Returns a renderer for the given shape instance, or null if none is registered.
     */
    public ShapeRenderer resolve(Shape shape) {
        Class<?> clazz = shape.getClass();
        while (clazz != null && Shape.class.isAssignableFrom(clazz)) {
            @SuppressWarnings("unchecked")
            Class<? extends Shape> shapeClass = (Class<? extends Shape>) clazz;
            ShapeRenderer renderer = renderers.get(shapeClass);
            if (renderer != null) {
                return renderer;
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }
}
