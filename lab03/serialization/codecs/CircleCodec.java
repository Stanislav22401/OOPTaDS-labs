package serialization.codecs;

import serialization.ShapeCodec;
import serialization.json.JsonValue;
import shapes.Circle;
import shapes.Shape;

import java.util.LinkedHashMap;
import java.util.Map;

/** JSON codec for {@link Circle}. */
public class CircleCodec implements ShapeCodec {
    @Override
    public String getTypeName() {
        return "Circle";
    }

    @Override
    public Class<? extends Shape> getShapeClass() {
        return Circle.class;
    }

    @Override
    public boolean supports(Shape shape) {
        return shape.getClass() == Circle.class;
    }

    @Override
    public Map<String, Object> toMap(Shape shape) {
        Circle circle = (Circle) shape;
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("x", circle.getX());
        map.put("y", circle.getY());
        map.put("diameter", circle.getDiameter());
        return map;
    }

    @Override
    public Shape fromMap(Map<String, Object> map) {
        return new Circle(
                JsonValue.asInt(map.get("x")),
                JsonValue.asInt(map.get("y")),
                JsonValue.asInt(map.get("diameter")));
    }
}
