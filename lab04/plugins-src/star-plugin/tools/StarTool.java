package tools;

import drawing.DrawingPanel;
import shapes.Star;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/** Mouse tool that creates a {@link Star} from center to outer radius. */
public class StarTool implements Tool {
    private int centerX;
    private int centerY;
    private int currentX;
    private int currentY;
    private boolean dragging;

    @Override
    public void mousePressed(MouseEvent e, DrawingPanel panel) {
        centerX = e.getX();
        centerY = e.getY();
        currentX = centerX;
        currentY = centerY;
        dragging = true;
    }

    @Override
    public void mouseDragged(MouseEvent e, DrawingPanel panel) {
        if (dragging) {
            currentX = e.getX();
            currentY = e.getY();
            panel.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e, DrawingPanel panel) {
        if (dragging) {
            currentX = e.getX();
            currentY = e.getY();
            int radius = (int) Math.hypot(currentX - centerX, currentY - centerY);
            if (radius > 0) {
                panel.getShapeList().addShape(new Star(centerX, centerY, radius));
                panel.notifyShapeListChanged();
            }
            reset();
            panel.repaint();
        }
    }

    @Override
    public void drawPreview(Graphics2D g) {
        if (!dragging) {
            return;
        }
        int radius = (int) Math.hypot(currentX - centerX, currentY - centerY);
        if (radius <= 0) {
            return;
        }
        Star preview = new Star(centerX, centerY, radius);
        g.drawPolygon(preview.getXPoints(), preview.getYPoints(), preview.getXPoints().length);
    }

    @Override
    public void reset() {
        dragging = false;
    }
}
