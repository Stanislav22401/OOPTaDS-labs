package serialization.codecs;

import serialization.ShapeCodec;
import serialization.json.JsonValue;
import shapes.Shape;
import shapes.Square;

import java.util.LinkedHashMap;
import java.util.Map;

/** JSON codec for {@link Square}. */
public class SquareCodec implements ShapeCodec {
    @Override
    public String getTypeName() {
        return "Square";
    }

    @Override
    public Class<? extends Shape> getShapeClass() {
        return Square.class;
    }

    @Override
    public boolean supports(Shape shape) {
        return shape.getClass() == Square.class;
    }

    @Override
    public Map<String, Object> toMap(Shape shape) {
        Square square = (Square) shape;
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("x", square.getX());
        map.put("y", square.getY());
        map.put("side", square.getSide());
        return map;
    }

    @Override
    public Shape fromMap(Map<String, Object> map) {
        return new Square(
                JsonValue.asInt(map.get("x")),
                JsonValue.asInt(map.get("y")),
                JsonValue.asInt(map.get("side")));
    }
}
