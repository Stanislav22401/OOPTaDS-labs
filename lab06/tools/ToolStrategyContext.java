package tools;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import drawing.DrawingPanel;

/**
 * Strategy pattern context: delegates mouse handling and preview to the active {@link Tool} strategy.
 */
public class ToolStrategyContext {
    private Tool activeStrategy;

    public void setStrategy(Tool strategy) {
        if (activeStrategy != null) {
            activeStrategy.reset();
        }
        activeStrategy = strategy;
    }

    public Tool getActiveStrategy() {
        return activeStrategy;
    }

    public void mousePressed(MouseEvent e, DrawingPanel panel) {
        if (activeStrategy != null) {
            activeStrategy.mousePressed(e, panel);
        }
    }

    public void mouseDragged(MouseEvent e, DrawingPanel panel) {
        if (activeStrategy != null) {
            activeStrategy.mouseDragged(e, panel);
        }
    }

    public void mouseReleased(MouseEvent e, DrawingPanel panel) {
        if (activeStrategy != null) {
            activeStrategy.mouseReleased(e, panel);
        }
    }

    public void drawPreview(Graphics2D g) {
        if (activeStrategy != null) {
            activeStrategy.drawPreview(g);
        }
    }
}
