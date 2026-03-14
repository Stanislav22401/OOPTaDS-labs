package drawing;

import shapes.Shape;
import rendering.ShapeRenderer;
import tools.Tool;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Map;

/**
 * The canvas where shapes are drawn and mouse events are handled.
 */
public class DrawingPanel extends JPanel {
    private final ShapeList shapeList;
    private final Map<Class<? extends Shape>, ShapeRenderer> renderers;
    private Tool currentTool;

    public DrawingPanel(ShapeList shapeList, Map<Class<? extends Shape>, ShapeRenderer> renderers) {
        this.shapeList = shapeList;
        this.renderers = renderers;

        // Add mouse listeners
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (currentTool != null) {
                    currentTool.mousePressed(e, DrawingPanel.this);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (currentTool != null) {
                    currentTool.mouseReleased(e, DrawingPanel.this);
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (currentTool != null) {
                    currentTool.mouseDragged(e, DrawingPanel.this);
                }
            }
        });
    }

    /**
     * Sets the active tool.
     */
    public void setTool(Tool tool) {
        if (currentTool != null) {
            currentTool.reset();
        }
        currentTool = tool;
        repaint();
    }

    public ShapeList getShapeList() {
        return shapeList;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Draw all finished shapes
        for (Shape shape : shapeList.getShapes()) {
            ShapeRenderer renderer = renderers.get(shape.getClass());
            if (renderer != null) {
                renderer.render(g2, shape);
            }
        }

        // Draw preview from current tool
        if (currentTool != null) {
            currentTool.drawPreview(g2);
        }
    }
}
