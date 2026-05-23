package serialization;

import shapes.Shape;
import java.util.Map;

/**
 * Encodes and decodes one concrete shape type to/from a property map (JSON object body).
 */
public interface ShapeCodec {
    /** Type name stored in the JSON {@code type} field. */
    String getTypeName();

    /** Concrete shape class handled by this codec. */
    Class<? extends Shape> getShapeClass();

    /** Returns true when this codec is responsible for the given instance. */
    boolean supports(Shape shape);

    /** Serializes shape properties (without the {@code type} field). */
    Map<String, Object> toMap(Shape shape);

    /** Creates a shape from properties (without the {@code type} field). */
    Shape fromMap(Map<String, Object> map);
}
