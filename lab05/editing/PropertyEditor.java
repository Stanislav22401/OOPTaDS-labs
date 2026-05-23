package editing;

import shapes.Shape;
import java.util.List;

/**
 * Builds and applies editable fields for one shape type.
 */
public interface PropertyEditor {
    boolean supports(Shape shape);
    List<PropertyField> readFields(Shape shape);
    void applyFields(Shape shape, List<PropertyField> fields);
}
