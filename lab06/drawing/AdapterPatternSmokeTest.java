package drawing;

import plugin.ClassmatePluginLoader;
import plugin.functional.FunctionPluginLoader;
import plugin.functional.FunctionPluginRegistry;
import serialization.JsonShapeListSerializer;
import serialization.ProcessingShapeListSerializer;
import shapes.Line;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Verifies classmate plugins are adapted and participate in the XML save/load pipeline.
 */
public class AdapterPatternSmokeTest {
    public static void main(String[] args) throws Exception {
        ApplicationBootstrap bootstrap = new ApplicationBootstrap();
        FunctionPluginRegistry registry = new FunctionPluginRegistry();
        new FunctionPluginLoader().loadFromDirectory(Path.of("function-plugins"), registry);
        new ClassmatePluginLoader().loadFromDirectory(Path.of("classmate-plugins"), registry);

        ShapeList list = new ShapeList();
        list.addShape(new Line(0, 0, 10, 10));

        JsonShapeListSerializer json = new JsonShapeListSerializer(bootstrap.getCodecRegistry());
        ProcessingShapeListSerializer processing = new ProcessingShapeListSerializer(json, registry);

        Path temp = Files.createTempFile("lab06-adapter", ".xml");
        processing.save(list, temp);
        String xml = Files.readString(temp);
        if (!xml.contains("Classmate Header Plugin")) {
            throw new IllegalStateException("Expected classmate header comment in saved XML");
        }

        ShapeList loaded = new ShapeList();
        processing.load(loaded, temp);
        Files.deleteIfExists(temp);

        if (loaded.size() != 1) {
            throw new IllegalStateException("Expected 1 shape after load");
        }
        System.out.println("Adapter + classmate plugin pipeline OK");
    }
}
