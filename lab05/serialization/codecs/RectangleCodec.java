package serialization.codecs;

import serialization.ShapeCodec;
import serialization.json.JsonValue;
import shapes.Rectangle;
import shapes.Shape;

import java.util.LinkedHashMap;
import java.util.Map;

/** JSON codec for {@link Rectangle}. */
public class RectangleCodec implements ShapeCodec {
    @Override
    public String getTypeName() {
        return "Rectangle";
    }

    @Override
    public Class<? extends Shape> getShapeClass() {
        return Rectangle.class;
    }

    @Override
    public boolean supports(Shape shape) {
        return shape.getClass() == Rectangle.class;
    }

    @Override
    public Map<String, Object> toMap(Shape shape) {
        Rectangle rect = (Rectangle) shape;
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("x", rect.getX());
        map.put("y", rect.getY());
        map.put("width", rect.getWidth());
        map.put("height", rect.getHeight());
        return map;
    }

    @Override
    public Shape fromMap(Map<String, Object> map) {
        return new Rectangle(
                JsonValue.asInt(map.get("x")),
                JsonValue.asInt(map.get("y")),
                JsonValue.asInt(map.get("width")),
                JsonValue.asInt(map.get("height")));
    }
}
