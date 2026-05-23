package drawing;

import plugin.PluginLoader;
import serialization.JsonShapeListSerializer;
import serialization.ShapeCodecRegistry;
import shapes.Shape;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Headless check that the star plugin registers its codec and JSON round-trip works.
 */
public class PluginLoadSmokeTest {
    public static void main(String[] args) throws Exception {
        ApplicationBootstrap bootstrap = new ApplicationBootstrap();
        PluginLoader loader = new PluginLoader();
        Path jar = Paths.get("plugins/star-plugin.jar");
        String name = loader.loadJar(jar, bootstrap);
        System.out.println("Loaded: " + name);

        ShapeCodecRegistry codecs = bootstrap.getCodecRegistry();
        Map<String, Object> props = new LinkedHashMap<>();
        props.put("centerX", 100);
        props.put("centerY", 100);
        props.put("outerRadius", 50);
        Shape star = codecs.getByTypeName("Star").fromMap(props);

        ShapeList list = new ShapeList();
        list.addShape(star);

        Path temp = Files.createTempFile("lab04-star", ".json");
        new JsonShapeListSerializer(codecs).save(list, temp);

        ShapeList loaded = new ShapeList();
        new JsonShapeListSerializer(codecs).load(loaded, temp);
        Files.deleteIfExists(temp);

        if (loaded.size() != 1) {
            throw new IllegalStateException("Expected 1 shape, got " + loaded.size());
        }
        if (!"Star".equals(codecs.findCodec(loaded.get(0)).getTypeName())) {
            throw new IllegalStateException("Loaded shape is not Star");
        }
        System.out.println("Star plugin JSON round-trip OK");
    }
}
