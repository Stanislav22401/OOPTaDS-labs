package tools;

import drawing.DrawingPanel;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 * Interface for a drawing tool that creates shapes via mouse interaction.
 */
public interface Tool {
    void mousePressed(MouseEvent e, DrawingPanel panel);
    void mouseDragged(MouseEvent e, DrawingPanel panel);
    void mouseReleased(MouseEvent e, DrawingPanel panel);
    void drawPreview(Graphics2D g);
    void reset();
}
