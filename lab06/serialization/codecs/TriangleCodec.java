package serialization.codecs;

import serialization.ShapeCodec;
import serialization.json.JsonValue;
import shapes.Shape;
import shapes.Triangle;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/** JSON codec for {@link Triangle}. */
public class TriangleCodec implements ShapeCodec {
    @Override
    public String getTypeName() {
        return "Triangle";
    }

    @Override
    public Class<? extends Shape> getShapeClass() {
        return Triangle.class;
    }

    @Override
    public boolean supports(Shape shape) {
        return shape.getClass() == Triangle.class;
    }

    @Override
    public Map<String, Object> toMap(Shape shape) {
        Triangle tri = (Triangle) shape;
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("xPoints", Arrays.asList(
                box(tri.getXPoints()[0]), box(tri.getXPoints()[1]), box(tri.getXPoints()[2])));
        map.put("yPoints", Arrays.asList(
                box(tri.getYPoints()[0]), box(tri.getYPoints()[1]), box(tri.getYPoints()[2])));
        return map;
    }

    @Override
    public Shape fromMap(Map<String, Object> map) {
        int[] xPoints = JsonValue.asIntArray(map.get("xPoints"));
        int[] yPoints = JsonValue.asIntArray(map.get("yPoints"));
        return new Triangle(xPoints, yPoints);
    }

    private static Integer box(int value) {
        return value;
    }
}
