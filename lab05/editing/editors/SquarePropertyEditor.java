package editing.editors;

import editing.PropertyEditor;
import editing.PropertyField;
import shapes.Shape;
import shapes.Square;

import java.util.Arrays;
import java.util.List;

/** Property editor for {@link Square}. */
public class SquarePropertyEditor implements PropertyEditor {
    @Override
    public boolean supports(Shape shape) {
        return shape instanceof Square;
    }

    @Override
    public List<PropertyField> readFields(Shape shape) {
        Square square = (Square) shape;
        return Arrays.asList(
                new PropertyField("x", "X", String.valueOf(square.getX())),
                new PropertyField("y", "Y", String.valueOf(square.getY())),
                new PropertyField("side", "Side", String.valueOf(square.getSide())));
    }

    @Override
    public void applyFields(Shape shape, List<PropertyField> fields) {
        Square square = (Square) shape;
        square.setX(parseInt(fields, "x"));
        square.setY(parseInt(fields, "y"));
        square.setSide(parseInt(fields, "side"));
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
