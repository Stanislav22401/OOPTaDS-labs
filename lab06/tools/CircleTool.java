package tools;

import shapes.Circle;
import drawing.DrawingPanel;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/** Tool for creating a circle by dragging from center to radius. */
public class CircleTool implements Tool {
    private int centerX, centerY;
    private int currentX, currentY;
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
            int x = centerX - radius;
            int y = centerY - radius;
            int diameter = 2 * radius;
            panel.getShapeList().addShape(new Circle(x, y, diameter));
            reset();
            panel.repaint();
        }
    }

    @Override
    public void drawPreview(Graphics2D g) {
        if (dragging) {
            int radius = (int) Math.hypot(currentX - centerX, currentY - centerY);
            int x = centerX - radius;
            int y = centerY - radius;
            int diameter = 2 * radius;
            g.drawOval(x, y, diameter, diameter);
        }
    }

    @Override
    public void reset() {
        dragging = false;
    }
}
