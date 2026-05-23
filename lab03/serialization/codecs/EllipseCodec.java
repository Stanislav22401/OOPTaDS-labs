package serialization.codecs;

import serialization.ShapeCodec;
import serialization.json.JsonValue;
import shapes.Ellipse;
import shapes.Shape;

import java.util.LinkedHashMap;
import java.util.Map;

/** JSON codec for {@link Ellipse}. */
public class EllipseCodec implements ShapeCodec {
    @Override
    public String getTypeName() {
        return "Ellipse";
    }

    @Override
    public Class<? extends Shape> getShapeClass() {
        return Ellipse.class;
    }

    @Override
    public boolean supports(Shape shape) {
        return shape.getClass() == Ellipse.class;
    }

    @Override
    public Map<String, Object> toMap(Shape shape) {
        Ellipse ell = (Ellipse) shape;
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("x", ell.getX());
        map.put("y", ell.getY());
        map.put("width", ell.getWidth());
        map.put("height", ell.getHeight());
        return map;
    }

    @Override
    public Shape fromMap(Map<String, Object> map) {
        return new Ellipse(
                JsonValue.asInt(map.get("x")),
                JsonValue.asInt(map.get("y")),
                JsonValue.asInt(map.get("width")),
                JsonValue.asInt(map.get("height")));
    }
}
