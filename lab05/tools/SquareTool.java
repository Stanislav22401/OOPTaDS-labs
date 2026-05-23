package tools;

import shapes.Square;
import drawing.DrawingPanel;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/** Tool for creating a square by dragging from one corner. */
public class SquareTool implements Tool {
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
            int side = Math.max(Math.abs(currentX - startX), Math.abs(currentY - startY));
            int x2 = startX + (currentX > startX ? side : -side);
            int y2 = startY + (currentY > startY ? side : -side);
            int finalX = Math.min(startX, x2);
            int finalY = Math.min(startY, y2);
            panel.getShapeList().addShape(new Square(finalX, finalY, side));
            reset();
            panel.notifyShapeListChanged();
            panel.repaint();
        }
    }

    @Override
    public void drawPreview(Graphics2D g) {
        if (dragging) {
            int side = Math.max(Math.abs(currentX - startX), Math.abs(currentY - startY));
            int x2 = startX + (currentX > startX ? side : -side);
            int y2 = startY + (currentY > startY ? side : -side);
            int x = Math.min(startX, x2);
            int y = Math.min(startY, y2);
            g.drawRect(x, y, side, side);
        }
    }

    @Override
    public void reset() {
        dragging = false;
    }
}
