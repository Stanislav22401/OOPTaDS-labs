package drawing;

import rendering.RendererRegistry;
import rendering.ShapeRenderer;
import shapes.Shape;
import tools.Tool;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Canvas where shapes are drawn and mouse events are handled.
 */
public class DrawingPanel extends JPanel {
    private final ShapeList shapeList;
    private final RendererRegistry rendererRegistry;
    private final List<Runnable> shapeListListeners = new ArrayList<>();
    private Tool currentTool;

    public DrawingPanel(ShapeList shapeList, RendererRegistry rendererRegistry) {
        this.shapeList = shapeList;
        this.rendererRegistry = rendererRegistry;

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

    public void addShapeListListener(Runnable listener) {
        shapeListListeners.add(listener);
    }

    public void notifyShapeListChanged() {
        for (Runnable listener : shapeListListeners) {
            listener.run();
        }
    }

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

        for (Shape shape : shapeList.getShapes()) {
            ShapeRenderer renderer = rendererRegistry.resolve(shape);
            if (renderer != null) {
                renderer.render(g2, shape);
            }
        }

        if (currentTool != null) {
            currentTool.drawPreview(g2);
        }
    }
}
