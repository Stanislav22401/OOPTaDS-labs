package editing.editors;

import editing.PropertyEditor;
import editing.PropertyField;
import shapes.Shape;
import shapes.Triangle;

import java.util.ArrayList;
import java.util.List;

/** Property editor for {@link Triangle}. */
public class TrianglePropertyEditor implements PropertyEditor {
    @Override
    public boolean supports(Shape shape) {
        return shape instanceof Triangle;
    }

    @Override
    public List<PropertyField> readFields(Shape shape) {
        Triangle tri = (Triangle) shape;
        int[] xs = tri.getXPoints();
        int[] ys = tri.getYPoints();
        List<PropertyField> fields = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            fields.add(new PropertyField("x" + i, "X" + (i + 1), String.valueOf(xs[i])));
            fields.add(new PropertyField("y" + i, "Y" + (i + 1), String.valueOf(ys[i])));
        }
        return fields;
    }

    @Override
    public void applyFields(Shape shape, List<PropertyField> fields) {
        Triangle tri = (Triangle) shape;
        int[] xs = new int[3];
        int[] ys = new int[3];
        for (int i = 0; i < 3; i++) {
            xs[i] = parseInt(fields, "x" + i);
            ys[i] = parseInt(fields, "y" + i);
        }
        tri.setXPoints(xs);
        tri.setYPoints(ys);
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
