package shapes;

import java.awt.Graphics;

public class EllipseShape extends Shape {
    protected int x, y, width, height;

    public EllipseShape(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(Graphics g) {
        g.drawOval(x, y, width, height);
    }
}
