package tools;

import shapes.Line;
import drawing.DrawingPanel;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/** Tool for creating a line by dragging between two points. */
public class LineTool implements Tool {
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
            panel.getShapeList().addShape(new Line(startX, startY, currentX, currentY));
            reset();
            panel.notifyShapeListChanged();
            panel.repaint();
        }
    }

    @Override
    public void drawPreview(Graphics2D g) {
        if (dragging) {
            g.drawLine(startX, startY, currentX, currentY);
        }
    }

    @Override
    public void reset() {
        dragging = false;
    }
}
