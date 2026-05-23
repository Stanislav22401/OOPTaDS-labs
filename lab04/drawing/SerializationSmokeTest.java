package drawing;

import serialization.JsonShapeListSerializer;
import shapes.*;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Headless smoke test for JSON save/load round-trip.
 */
public class SerializationSmokeTest {
    public static void main(String[] args) throws Exception {
        ApplicationBootstrap bootstrap = new ApplicationBootstrap();
        ShapeList list = new ShapeList();
        list.addShape(new Line(1, 2, 3, 4));
        list.addShape(new Rectangle(10, 20, 30, 40));
        list.addShape(new Square(5, 5, 25));
        list.addShape(new Circle(100, 100, 50));
        list.addShape(new Ellipse(200, 50, 80, 40));
        list.addShape(new Triangle(new int[]{0, 50, 25}, new int[]{0, 0, 40}));

        Path temp = Files.createTempFile("lab03-shapes", ".json");
        JsonShapeListSerializer serializer = new JsonShapeListSerializer(bootstrap.getCodecRegistry());
        serializer.save(list, temp);
        String json = Files.readString(temp);

        ShapeList loaded = new ShapeList();
        serializer.load(loaded, temp);
        Files.deleteIfExists(temp);

        if (loaded.size() != 6) {
            throw new IllegalStateException("Expected 6 shapes, got " + loaded.size());
        }
        System.out.println("JSON round-trip OK (" + loaded.size() + " shapes)");
        System.out.println(json);
    }
}
