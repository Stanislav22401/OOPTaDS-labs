package editing;

/**
 * Describes one editable property shown in the property dialog.
 */
public class PropertyField {
    private final String key;
    private final String label;
    private String value;

    public PropertyField(String key, String label, String value) {
        this.key = key;
        this.label = label;
        this.value = value;
    }

    public String getKey() { return key; }
    public String getLabel() { return label; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}
