package serialization;

import serialization.json.JsonValue;
import shapes.Shape;
import shapes.Star;

import java.util.LinkedHashMap;
import java.util.Map;

/** JSON codec for plugin {@link Star} shapes. */
public class StarCodec implements ShapeCodec {
    @Override
    public String getTypeName() {
        return "Star";
    }

    @Override
    public Class<? extends Shape> getShapeClass() {
        return Star.class;
    }

    @Override
    public boolean supports(Shape shape) {
        return shape.getClass().getName().equals(Star.class.getName());
    }

    @Override
    public Map<String, Object> toMap(Shape shape) {
        Star star = (Star) shape;
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("centerX", star.getCenterX());
        map.put("centerY", star.getCenterY());
        map.put("outerRadius", star.getOuterRadius());
        return map;
    }

    @Override
    public Shape fromMap(Map<String, Object> map) {
        return new Star(
                JsonValue.asInt(map.get("centerX")),
                JsonValue.asInt(map.get("centerY")),
                JsonValue.asInt(map.get("outerRadius")));
    }
}
