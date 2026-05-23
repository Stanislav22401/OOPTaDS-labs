package serialization;

import drawing.ShapeList;
import serialization.json.JsonValue;
import shapes.Shape;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Saves and loads a {@link ShapeList} using JSON format (lab variant 4).
 */
public class JsonShapeListSerializer {
    private final ShapeCodecRegistry codecRegistry;

    public JsonShapeListSerializer(ShapeCodecRegistry codecRegistry) {
        this.codecRegistry = codecRegistry;
    }

    /**
     * Serializes all shapes to a JSON file.
     */
    public void save(ShapeList shapeList, Path file) throws IOException {
        List<Map<String, Object>> items = new ArrayList<>();
        for (Shape shape : shapeList.getShapes()) {
            ShapeCodec codec = codecRegistry.findCodec(shape);
            Map<String, Object> entry = new LinkedHashMap<>(codec.toMap(shape));
            entry.put("type", codec.getTypeName());
            items.add(entry);
        }
        Map<String, Object> root = new LinkedHashMap<>();
        root.put("shapes", items);
        String json = JsonValue.stringify(root);
        Files.writeString(file, json, StandardCharsets.UTF_8);
    }

    /**
     * Replaces the contents of the shape list with shapes read from a JSON file.
     */
    public void load(ShapeList shapeList, Path file) throws IOException {
        String json = Files.readString(file, StandardCharsets.UTF_8);
        Object parsed = JsonValue.parse(json);
        Map<String, Object> root = JsonValue.asObject(parsed);
        List<Object> rawItems = JsonValue.asArray(root.get("shapes"));
        shapeList.clear();
        for (Object rawItem : rawItems) {
            Map<String, Object> map = JsonValue.asObject(rawItem);
            String typeName = JsonValue.asString(map.get("type"));
            Map<String, Object> properties = new LinkedHashMap<>(map);
            properties.remove("type");
            ShapeCodec codec = codecRegistry.getByTypeName(typeName);
            shapeList.addShape(codec.fromMap(properties));
        }
    }
}
