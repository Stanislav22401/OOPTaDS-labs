package editing;

import shapes.Shape;
import java.util.ArrayList;
import java.util.List;

/**
 * Registry of property editors. Dispatch uses iteration — no switch/case or reflection.
 */
public class PropertyEditorRegistry {
    private final List<PropertyEditor> editors = new ArrayList<>();

    public void register(PropertyEditor editor) {
        editors.add(editor);
    }

    public PropertyEditor findEditor(Shape shape) {
        for (PropertyEditor editor : editors) {
            if (editor.supports(shape)) {
                return editor;
            }
        }
        throw new IllegalArgumentException("No property editor for shape: " + shape.getClass().getName());
    }
}
