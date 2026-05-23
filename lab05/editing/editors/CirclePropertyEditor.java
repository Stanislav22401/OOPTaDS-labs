package editing.editors;

import editing.PropertyEditor;
import editing.PropertyField;
import shapes.Circle;
import shapes.Shape;

import java.util.Arrays;
import java.util.List;

/** Property editor for {@link Circle}. */
public class CirclePropertyEditor implements PropertyEditor {
    @Override
    public boolean supports(Shape shape) {
        return shape instanceof Circle;
    }

    @Override
    public List<PropertyField> readFields(Shape shape) {
        Circle circle = (Circle) shape;
        return Arrays.asList(
                new PropertyField("x", "X", String.valueOf(circle.getX())),
                new PropertyField("y", "Y", String.valueOf(circle.getY())),
                new PropertyField("diameter", "Diameter", String.valueOf(circle.getDiameter())));
    }

    @Override
    public void applyFields(Shape shape, List<PropertyField> fields) {
        Circle circle = (Circle) shape;
        circle.setX(parseInt(fields, "x"));
        circle.setY(parseInt(fields, "y"));
        circle.setDiameter(parseInt(fields, "diameter"));
    }

    private static int parseInt(List<PropertyField> fields, String key) {
        for (PropertyField field : fields) {
            if (field.getKey().equals(key)) {
                return Integer.parseInt(field.getValue().trim());
            }
        }
        throw new IllegalArgumentException("Missing field: " + key);
    }
}
