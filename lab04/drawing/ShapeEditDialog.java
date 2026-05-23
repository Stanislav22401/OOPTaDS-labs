package drawing;

import editing.PropertyEditor;
import editing.PropertyEditorRegistry;
import editing.PropertyField;
import shapes.Shape;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dialog for editing shape properties using the registered property editor.
 */
public class ShapeEditDialog extends JDialog {
    private boolean confirmed;

    public ShapeEditDialog(java.awt.Frame owner, Shape shape, PropertyEditorRegistry editorRegistry) {
        super(owner, "Edit shape", true);
        PropertyEditor editor = editorRegistry.findEditor(shape);
        List<PropertyField> fields = new ArrayList<>(editor.readFields(shape));
        Map<String, JTextField> inputs = new HashMap<>();

        JPanel form = new JPanel(new GridLayout(fields.size(), 2, 8, 8));
        for (PropertyField field : fields) {
            form.add(new JLabel(field.getLabel() + ":"));
            JTextField textField = new JTextField(field.getValue(), 10);
            inputs.put(field.getKey(), textField);
            form.add(textField);
        }

        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");
        ok.addActionListener(e -> {
            for (PropertyField field : fields) {
                field.setValue(inputs.get(field.getKey()).getText());
            }
            editor.applyFields(shape, fields);
            confirmed = true;
            dispose();
        });
        cancel.addActionListener(e -> dispose());

        JPanel buttons = new JPanel();
        buttons.add(ok);
        buttons.add(cancel);

        setLayout(new BorderLayout(10, 10));
        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(owner);
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
