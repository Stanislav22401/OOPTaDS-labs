package plugin;

import editing.PropertyEditor;
import rendering.ShapeRenderer;
import serialization.ShapeCodec;
import shapes.Shape;
import tools.Tool;

import java.util.function.Supplier;

/**
 * Host API exposed to plugins for registering new shape types and UI integration.
 */
public interface PluginContext {
    void registerCodec(ShapeCodec codec);

    void registerPropertyEditor(PropertyEditor editor);

    void registerRenderer(Class<? extends Shape> shapeClass, ShapeRenderer renderer);

    void registerTool(String toolbarLabel, Supplier<Tool> toolFactory);
}
