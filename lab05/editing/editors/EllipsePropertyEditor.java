package editing.editors;

import editing.PropertyEditor;
import editing.PropertyField;
import shapes.Ellipse;
import shapes.Shape;

import java.util.Arrays;
import java.util.List;

/** Property editor for {@link Ellipse}. */
public class EllipsePropertyEditor implements PropertyEditor {
    @Override
    public boolean supports(Shape shape) {
        return shape.getClass() == Ellipse.class;
    }

    @Override
    public List<PropertyField> readFields(Shape shape) {
        Ellipse ell = (Ellipse) shape;
        return Arrays.asList(
                new PropertyField("x", "X", String.valueOf(ell.getX())),
                new PropertyField("y", "Y", String.valueOf(ell.getY())),
                new PropertyField("width", "Width", String.valueOf(ell.getWidth())),
                new PropertyField("height", "Height", String.valueOf(ell.getHeight())));
    }

    @Override
    public void applyFields(Shape shape, List<PropertyField> fields) {
        Ellipse ell = (Ellipse) shape;
        ell.setX(parseInt(fields, "x"));
        ell.setY(parseInt(fields, "y"));
        ell.setWidth(parseInt(fields, "width"));
        ell.setHeight(parseInt(fields, "height"));
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
