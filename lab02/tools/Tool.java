package tools;

import drawing.DrawingPanel;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 * Interface for a drawing tool that creates shapes via mouse interaction.
 */
public interface Tool {
    /**
     * Called when mouse button is pressed on the drawing panel.
     */
    void mousePressed(MouseEvent e, DrawingPanel panel);

    /**
     * Called when mouse is dragged (button held and moved).
     */
    void mouseDragged(MouseEvent e, DrawingPanel panel);

    /**
     * Called when mouse button is released.
     */
    void mouseReleased(MouseEvent e, DrawingPanel panel);

    /**
     * Draws a preview of the shape being created (e.g., rubber-band line).
     */
    void drawPreview(Graphics2D g);

    /**
     * Resets the tool's internal state (e.g., after shape is finished).
     */
    void reset();
}
