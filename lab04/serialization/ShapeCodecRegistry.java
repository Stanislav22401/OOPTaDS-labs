package serialization;

import shapes.Shape;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Registry of shape codecs. Dispatch uses maps and iteration — no switch/case or reflection.
 */
public class ShapeCodecRegistry {
    private final Map<String, ShapeCodec> byTypeName = new HashMap<>();
    private final List<ShapeCodec> codecs = new ArrayList<>();

    public void register(ShapeCodec codec) {
        byTypeName.put(codec.getTypeName(), codec);
        codecs.add(codec);
    }

    public ShapeCodec findCodec(Shape shape) {
        for (ShapeCodec codec : codecs) {
            if (codec.supports(shape)) {
                return codec;
            }
        }
        throw new IllegalArgumentException("No codec registered for shape: " + shape.getClass().getName());
    }

    public ShapeCodec getByTypeName(String typeName) {
        ShapeCodec codec = byTypeName.get(typeName);
        if (codec == null) {
            throw new IllegalArgumentException("Unknown shape type in JSON: " + typeName);
        }
        return codec;
    }
}
