package classmate.api;

/**
 * External functional plugin API (simulates a classmate's module with a different interface).
 * Host application integrates these plugins via the Adapter pattern only.
 */
public interface XmlPipelinePlugin {
    String getPluginTitle();

    String getPluginVersion();

    String getSettingsLabel();

    boolean isActiveByDefault();

    /** Called when the drawing is exported to XML (before write). */
    String onExport(String xml) throws Exception;

    /** Called when a drawing is imported from XML (after read). */
    String onImport(String xml) throws Exception;
}
