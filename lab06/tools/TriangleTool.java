package tools;

import shapes.Triangle;
import drawing.DrawingPanel;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/** Tool for creating a triangle with three mouse clicks. */
public class TriangleTool implements Tool {
    private final List<Point> points = new ArrayList<>();
    private Point tempPoint;

    @Override
    public void mousePressed(MouseEvent e, DrawingPanel panel) {
        if (points.size() < 3) {
            points.add(new Point(e.getX(), e.getY()));
            tempPoint = null;
            if (points.size() == 3) {
                panel.getShapeList().addShape(
                        new Triangle(points.get(0), points.get(1), points.get(2)));
                reset();
            }
            panel.repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e, DrawingPanel panel) {
        if (points.size() < 3) {
            tempPoint = new Point(e.getX(), e.getY());
            panel.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e, DrawingPanel panel) {
        // Points are added on press for this tool.
    }

    @Override
    public void drawPreview(Graphics2D g) {
        int size = points.size();
        for (int i = 0; i < size; i++) {
            Point p = points.get(i);
            g.fillOval(p.x - 2, p.y - 2, 4, 4);
            if (i > 0) {
                Point prev = points.get(i - 1);
                g.drawLine(prev.x, prev.y, p.x, p.y);
            }
        }
        if (tempPoint != null && size > 0 && size < 3) {
            Point last = points.get(size - 1);
            g.drawLine(last.x, last.y, tempPoint.x, tempPoint.y);
            g.fillOval(tempPoint.x - 2, tempPoint.y - 2, 4, 4);
        }
        if (size == 2 && tempPoint != null) {
            Point first = points.get(0);
            g.drawLine(first.x, first.y, tempPoint.x, tempPoint.y);
        }
    }

    @Override
    public void reset() {
        points.clear();
        tempPoint = null;
    }
}
