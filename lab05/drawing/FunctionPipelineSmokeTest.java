package drawing;

import plugin.functional.FunctionPluginLoader;
import plugin.functional.FunctionPluginRegistry;
import serialization.JsonShapeListSerializer;
import serialization.ProcessingShapeListSerializer;
import shapes.Line;
import shapes.Rectangle;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Headless test: JSON → XML → XSLT pipeline → round-trip shapes.
 */
public class FunctionPipelineSmokeTest {
    public static void main(String[] args) throws Exception {
        ApplicationBootstrap bootstrap = new ApplicationBootstrap();
        FunctionPluginRegistry registry = new FunctionPluginRegistry();
        new FunctionPluginLoader().loadFromDirectory(Path.of("function-plugins"), registry);

        ShapeList list = new ShapeList();
        list.addShape(new Rectangle(10, 20, 30, 40));
        list.addShape(new Line(1, 2, 3, 4));

        JsonShapeListSerializer json = new JsonShapeListSerializer(bootstrap.getCodecRegistry());
        ProcessingShapeListSerializer serializer = new ProcessingShapeListSerializer(json, registry);

        Path temp = Files.createTempFile("lab05-shapes", ".xml");
        serializer.save(list, temp);
        String savedXml = Files.readString(temp);
        System.out.println("Saved XML preview:\n" + savedXml.substring(0, Math.min(400, savedXml.length())) + "...");

        ShapeList loaded = new ShapeList();
        serializer.load(loaded, temp);
        Files.deleteIfExists(temp);

        if (loaded.size() != 2) {
            throw new IllegalStateException("Expected 2 shapes, got " + loaded.size());
        }
        System.out.println("XSLT pipeline round-trip OK (" + loaded.size() + " shapes)");
    }
}
