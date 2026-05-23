package tools;

import shapes.Ellipse;
import drawing.DrawingPanel;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/** Tool for creating an ellipse by dragging its bounding rectangle. */
public class EllipseTool implements Tool {
    private int startX, startY;
    private int currentX, currentY;
    private boolean dragging;

    @Override
    public void mousePressed(MouseEvent e, DrawingPanel panel) {
        startX = e.getX();
        startY = e.getY();
        currentX = startX;
        currentY = startY;
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
            int x = Math.min(startX, currentX);
            int y = Math.min(startY, currentY);
            int w = Math.abs(currentX - startX);
            int h = Math.abs(currentY - startY);
            panel.getShapeList().addShape(new Ellipse(x, y, w, h));
            reset();
            panel.notifyShapeListChanged();
            panel.repaint();
        }
    }

    @Override
    public void drawPreview(Graphics2D g) {
        if (dragging) {
            int x = Math.min(startX, currentX);
            int y = Math.min(startY, currentY);
            int w = Math.abs(currentX - startX);
            int h = Math.abs(currentY - startY);
            g.drawOval(x, y, w, h);
        }
    }

    @Override
    public void reset() {
        dragging = false;
    }
}
