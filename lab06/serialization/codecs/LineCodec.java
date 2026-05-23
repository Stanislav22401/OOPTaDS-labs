package serialization.codecs;

import serialization.ShapeCodec;
import shapes.Line;
import shapes.Shape;
import serialization.json.JsonValue;

import java.util.LinkedHashMap;
import java.util.Map;

/** JSON codec for {@link Line}. */
public class LineCodec implements ShapeCodec {
    @Override
    public String getTypeName() {
        return "Line";
    }

    @Override
    public Class<? extends Shape> getShapeClass() {
        return Line.class;
    }

    @Override
    public boolean supports(Shape shape) {
        return shape.getClass() == Line.class;
    }

    @Override
    public Map<String, Object> toMap(Shape shape) {
        Line line = (Line) shape;
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("x1", line.getX1());
        map.put("y1", line.getY1());
        map.put("x2", line.getX2());
        map.put("y2", line.getY2());
        return map;
    }

    @Override
    public Shape fromMap(Map<String, Object> map) {
        return new Line(
                JsonValue.asInt(map.get("x1")),
                JsonValue.asInt(map.get("y1")),
                JsonValue.asInt(map.get("x2")),
                JsonValue.asInt(map.get("y2")));
    }
}
