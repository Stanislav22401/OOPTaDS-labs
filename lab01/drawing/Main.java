package drawing;

import shapes.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;

public class Main {
    public static void main(String[] args) {
        // Статическая инициализация списка фигур
        ShapeList shapeList = new ShapeList();
        shapeList.addShape(new LineShape(50, 50, 150, 50));
        shapeList.addShape(new RectangleShape(50, 80, 100, 60));
        shapeList.addShape(new EllipseShape(50, 160, 100, 60));
        shapeList.addShape(new TriangleShape(200, 50, 250, 100, 150, 100));
        shapeList.addShape(new SquareShape(200, 120, 60));
        shapeList.addShape(new CircleShape(220, 200, 50));

        // Создание окна
        JFrame frame = new JFrame("Drawing Shapes");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.add(new DrawingPanel(shapeList));
        frame.setVisible(true);
    }
}

// Внутренний класс панели для рисования
class DrawingPanel extends JPanel {
    private ShapeList shapeList;

    public DrawingPanel(ShapeList shapeList) {
        this.shapeList = shapeList;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        shapeList.drawAll(g);
    }
}
