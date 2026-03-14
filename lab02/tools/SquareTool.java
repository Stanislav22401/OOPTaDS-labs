package tools;

import shapes.Square;
import drawing.DrawingPanel;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 * Tool for creating a square by two opposite corners.
 * The side length is the maximum of the width and height.
 */
public class SquareTool implements Tool {
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
            int dx = Math.abs(currentX - startX);
            int dy = Math.abs(currentY - startY);
            int side = Math.max(dx, dy);
            int x = startX < currentX ? startX : startX - side; // adjust to keep square within drag direction
            int y = startY < currentY ? startY : startY - side;
            // Simple approach: use top-left based on start point and side
            // More intuitive: keep the start point as one corner, and compute opposite corner based on sign
            int x2 = startX + (currentX > startX ? side : -side);
            int y2 = startY + (currentY > startY ? side : -side);
            int finalX = Math.min(startX, x2);
            int finalY = Math.min(startY, y2);
            Square sq = new Square(finalX, finalY, side);
            panel.getShapeList().addShape(sq);
            reset();
            panel.repaint();
        }
    }

    @Override
    public void drawPreview(Graphics2D g) {
        if (dragging) {
            int dx = Math.abs(currentX - startX);
            int dy = Math.abs(currentY - startY);
            int side = Math.max(dx, dy);
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
