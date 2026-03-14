package drawing;

import shapes.*;
import rendering.*;
import tools.*;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Main application window.
 */
public class MainFrame extends JFrame {
    private final ShapeList shapeList = new ShapeList();
    private final Map<Class<? extends Shape>, ShapeRenderer> renderers = new HashMap<>();
    private final DrawingPanel drawingPanel;

    public MainFrame() {
        setTitle("Simple Graphics Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        // Register renderers for each shape type
        registerRenderers();

        // Create drawing panel
        drawingPanel = new DrawingPanel(shapeList, renderers);
        add(drawingPanel, BorderLayout.CENTER);

        // Create tool bar and register tools
        JToolBar toolBar = createToolBar();
        add(toolBar, BorderLayout.NORTH);
    }

    /**
     * Registers a renderer for each shape class.
     * Subclasses (Square, Circle) reuse parent renderers.
     */
    private void registerRenderers() {
        renderers.put(Line.class, new LineRenderer());
        renderers.put(Rectangle.class, new RectangleRenderer());
        renderers.put(Ellipse.class, new EllipseRenderer());
        renderers.put(Triangle.class, new TriangleRenderer());
        renderers.put(Square.class, new RectangleRenderer()); // Square uses same renderer as Rectangle
        renderers.put(Circle.class, new EllipseRenderer());   // Circle uses same renderer as Ellipse
    }

    /**
     * Creates a toolbar with buttons for each tool.
     * Each button sets the corresponding tool in the drawing panel.
     */
    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar("Tools");

        // Create a button for each tool
        addToolButton(toolBar, "Line", new LineTool());
        addToolButton(toolBar, "Rectangle", new RectangleTool());
        addToolButton(toolBar, "Ellipse", new EllipseTool());
        addToolButton(toolBar, "Triangle", new TriangleTool());
        addToolButton(toolBar, "Square", new SquareTool());
        addToolButton(toolBar, "Circle", new CircleTool());

        return toolBar;
    }

    /**
     * Helper to add a tool button.
     */
    private void addToolButton(JToolBar toolBar, String name, Tool tool) {
        JButton button = new JButton(name);
        button.addActionListener((ActionEvent e) -> drawingPanel.setTool(tool));
        toolBar.add(button);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
