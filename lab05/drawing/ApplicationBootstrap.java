package drawing;

import editing.PropertyEditor;
import editing.PropertyEditorRegistry;
import editing.editors.CirclePropertyEditor;
import editing.editors.EllipsePropertyEditor;
import editing.editors.LinePropertyEditor;
import editing.editors.RectanglePropertyEditor;
import editing.editors.SquarePropertyEditor;
import editing.editors.TrianglePropertyEditor;
import plugin.PluginContext;
import rendering.EllipseRenderer;
import rendering.LineRenderer;
import rendering.RectangleRenderer;
import rendering.RendererRegistry;
import rendering.ShapeRenderer;
import rendering.TriangleRenderer;
import serialization.ShapeCodec;
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
import shapes.Shape;
import shapes.Triangle;
import tools.CircleTool;
import tools.EllipseTool;
import tools.LineTool;
import tools.RectangleTool;
import tools.SquareTool;
import tools.Tool;
import tools.TriangleTool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * Registers built-in shapes and accepts dynamic registrations from plugins via {@link PluginContext}.
 */
public final class ApplicationBootstrap implements PluginContext {
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

        registerCodec(new LineCodec());
        registerCodec(new RectangleCodec());
        registerCodec(new EllipseCodec());
        registerCodec(new TriangleCodec());
        registerCodec(new SquareCodec());
        registerCodec(new CircleCodec());

        registerPropertyEditor(new LinePropertyEditor());
        registerPropertyEditor(new RectanglePropertyEditor());
        registerPropertyEditor(new EllipsePropertyEditor());
        registerPropertyEditor(new TrianglePropertyEditor());
        registerPropertyEditor(new SquarePropertyEditor());
        registerPropertyEditor(new CirclePropertyEditor());

        registerTool("Line", LineTool::new);
        registerTool("Rectangle", RectangleTool::new);
        registerTool("Ellipse", EllipseTool::new);
        registerTool("Triangle", TriangleTool::new);
        registerTool("Square", SquareTool::new);
        registerTool("Circle", CircleTool::new);
    }

    @Override
    public void registerCodec(ShapeCodec codec) {
        codecRegistry.register(codec);
    }

    @Override
    public void registerPropertyEditor(PropertyEditor editor) {
        propertyEditorRegistry.register(editor);
    }

    @Override
    public void registerRenderer(Class<? extends Shape> shapeClass, ShapeRenderer renderer) {
        rendererRegistry.register(shapeClass, renderer);
    }

    @Override
    public void registerTool(String toolbarLabel, Supplier<Tool> toolFactory) {
        tools.add(new ToolEntry(toolbarLabel, toolFactory));
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
        return Collections.unmodifiableList(tools);
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
