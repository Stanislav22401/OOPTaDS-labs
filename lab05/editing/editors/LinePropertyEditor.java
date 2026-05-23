package editing.editors;

import editing.PropertyEditor;
import editing.PropertyField;
import shapes.Line;
import shapes.Shape;

import java.util.Arrays;
import java.util.List;

/** Property editor for {@link Line}. */
public class LinePropertyEditor implements PropertyEditor {
    @Override
    public boolean supports(Shape shape) {
        return shape instanceof Line;
    }

    @Override
    public List<PropertyField> readFields(Shape shape) {
        Line line = (Line) shape;
        return Arrays.asList(
                new PropertyField("x1", "X1", String.valueOf(line.getX1())),
                new PropertyField("y1", "Y1", String.valueOf(line.getY1())),
                new PropertyField("x2", "X2", String.valueOf(line.getX2())),
                new PropertyField("y2", "Y2", String.valueOf(line.getY2())));
    }

    @Override
    public void applyFields(Shape shape, List<PropertyField> fields) {
        Line line = (Line) shape;
        line.setX1(parseInt(fields, "x1"));
        line.setY1(parseInt(fields, "y1"));
        line.setX2(parseInt(fields, "x2"));
        line.setY2(parseInt(fields, "y2"));
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
