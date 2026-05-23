package editing;

import shapes.Shape;
import shapes.Star;

import java.util.Arrays;
import java.util.List;

/** Property editor for plugin {@link Star} shapes. */
public class StarPropertyEditor implements PropertyEditor {
    @Override
    public boolean supports(Shape shape) {
        return shape.getClass().getName().equals(Star.class.getName());
    }

    @Override
    public List<PropertyField> readFields(Shape shape) {
        Star star = (Star) shape;
        return Arrays.asList(
                new PropertyField("centerX", "Center X", String.valueOf(star.getCenterX())),
                new PropertyField("centerY", "Center Y", String.valueOf(star.getCenterY())),
                new PropertyField("outerRadius", "Outer radius", String.valueOf(star.getOuterRadius())));
    }

    @Override
    public void applyFields(Shape shape, List<PropertyField> fields) {
        Star star = (Star) shape;
        star.setCenterX(parseInt(fields, "centerX"));
        star.setCenterY(parseInt(fields, "centerY"));
        star.setOuterRadius(parseInt(fields, "outerRadius"));
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
