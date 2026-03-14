package tools;

import shapes.Rectangle;
import drawing.DrawingPanel;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 * Tool for creating a rectangle by two opposite corners.
 */
public class RectangleTool implements Tool {
    private int startX, startY;
    private int currentX, currentY;
    private boolean dragging = false;

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
            Rectangle rect = new Rectangle(x, y, w, h);
            panel.getShapeList().addShape(rect);
            reset();
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
            g.drawRect(x, y, w, h);
        }
    }

    @Override
    public void reset() {
        dragging = false;
    }
}
