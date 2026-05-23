package drawing;

import rendering.RendererRegistry;
import rendering.ShapeRenderer;
import shapes.Shape;
import tools.Tool;
import tools.ToolStrategyContext;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * Canvas: uses {@link ToolStrategyContext} (Strategy) and observes {@link ShapeList} (Observer).
 */
public class DrawingPanel extends JPanel {
    private final ShapeList shapeList;
    private final RendererRegistry rendererRegistry;
    private final ToolStrategyContext toolContext = new ToolStrategyContext();

    public DrawingPanel(ShapeList shapeList, RendererRegistry rendererRegistry) {
        this.shapeList = shapeList;
        this.rendererRegistry = rendererRegistry;
        shapeList.addObserver(source -> repaint());

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                toolContext.mousePressed(e, DrawingPanel.this);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                toolContext.mouseReleased(e, DrawingPanel.this);
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                toolContext.mouseDragged(e, DrawingPanel.this);
            }
        });
    }

    public void setTool(Tool tool) {
        toolContext.setStrategy(tool);
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

        toolContext.drawPreview(g2);
    }
}
