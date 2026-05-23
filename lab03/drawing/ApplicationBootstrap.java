package drawing;

import editing.PropertyEditorRegistry;
import editing.editors.CirclePropertyEditor;
import editing.editors.EllipsePropertyEditor;
import editing.editors.LinePropertyEditor;
import editing.editors.RectanglePropertyEditor;
import editing.editors.SquarePropertyEditor;
import editing.editors.TrianglePropertyEditor;
import rendering.EllipseRenderer;
import rendering.LineRenderer;
import rendering.RectangleRenderer;
import rendering.RendererRegistry;
import rendering.TriangleRenderer;
import serialization.ShapeCodecRegistry;
import serialization.codecs.CircleCodec;
import serialization.codecs.EllipseCodec;
import serialization.codecs.LineCodec;
import serialization.codecs.RectangleCodec;
import serialization.codecs.SquareCodec;
import serialization.codecs.TriangleCodec;
import shapes.Circle;
import shapes.Ellipse;
import shapes.Line;
import shapes.Rectangle;
import shapes.Triangle;
import tools.CircleTool;
import tools.EllipseTool;
import tools.LineTool;
import tools.RectangleTool;
import tools.SquareTool;
import tools.Tool;
import tools.TriangleTool;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Registers renderers, codecs, property editors, and tools for all built-in shapes.
 * Adding a new shape type requires only registration here plus new codec/editor classes.
 */
public final class ApplicationBootstrap {
    private final RendererRegistry rendererRegistry = new RendererRegistry();
    private final ShapeCodecRegistry codecRegistry = new ShapeCodecRegistry();
    private final PropertyEditorRegistry propertyEditorRegistry = new PropertyEditorRegistry();
    private final List<ToolEntry> tools = new ArrayList<>();

    public ApplicationBootstrap() {
        registerBuiltInShapes();
    }

    private void registerBuiltInShapes() {
        rendererRegistry.register(Line.class, new LineRenderer());
        rendererRegistry.register(Rectangle.class, new RectangleRenderer());
        rendererRegistry.register(Ellipse.class, new EllipseRenderer());
        rendererRegistry.register(Triangle.class, new TriangleRenderer());

        codecRegistry.register(new LineCodec());
        codecRegistry.register(new RectangleCodec());
        codecRegistry.register(new EllipseCodec());
        codecRegistry.register(new TriangleCodec());
        codecRegistry.register(new SquareCodec());
        codecRegistry.register(new CircleCodec());

        propertyEditorRegistry.register(new LinePropertyEditor());
        propertyEditorRegistry.register(new RectanglePropertyEditor());
        propertyEditorRegistry.register(new EllipsePropertyEditor());
        propertyEditorRegistry.register(new TrianglePropertyEditor());
        propertyEditorRegistry.register(new SquarePropertyEditor());
        propertyEditorRegistry.register(new CirclePropertyEditor());

        tools.add(new ToolEntry("Line", LineTool::new));
        tools.add(new ToolEntry("Rectangle", RectangleTool::new));
        tools.add(new ToolEntry("Ellipse", EllipseTool::new));
        tools.add(new ToolEntry("Triangle", TriangleTool::new));
        tools.add(new ToolEntry("Square", SquareTool::new));
        tools.add(new ToolEntry("Circle", CircleTool::new));
    }

    public RendererRegistry getRendererRegistry() {
        return rendererRegistry;
    }

    public ShapeCodecRegistry getCodecRegistry() {
        return codecRegistry;
    }

    public PropertyEditorRegistry getPropertyEditorRegistry() {
        return propertyEditorRegistry;
    }

    public List<ToolEntry> getTools() {
        return tools;
    }

    /** Label and factory for one drawing tool. */
    public static final class ToolEntry {
        private final String label;
        private final Supplier<Tool> factory;

        public ToolEntry(String label, Supplier<Tool> factory) {
            this.label = label;
            this.factory = factory;
        }

        public String getLabel() {
            return label;
        }

        public Tool createTool() {
            return factory.get();
        }
    }
}
