package editing.editors;

import editing.PropertyEditor;
import editing.PropertyField;
import shapes.Rectangle;
import shapes.Shape;

import java.util.Arrays;
import java.util.List;

/** Property editor for {@link Rectangle}. */
public class RectanglePropertyEditor implements PropertyEditor {
    @Override
    public boolean supports(Shape shape) {
        return shape.getClass() == Rectangle.class;
    }

    @Override
    public List<PropertyField> readFields(Shape shape) {
        Rectangle rect = (Rectangle) shape;
        return Arrays.asList(
                new PropertyField("x", "X", String.valueOf(rect.getX())),
                new PropertyField("y", "Y", String.valueOf(rect.getY())),
                new PropertyField("width", "Width", String.valueOf(rect.getWidth())),
                new PropertyField("height", "Height", String.valueOf(rect.getHeight())));
    }

    @Override
    public void applyFields(Shape shape, List<PropertyField> fields) {
        Rectangle rect = (Rectangle) shape;
        rect.setX(parseInt(fields, "x"));
        rect.setY(parseInt(fields, "y"));
        rect.setWidth(parseInt(fields, "width"));
        rect.setHeight(parseInt(fields, "height"));
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
