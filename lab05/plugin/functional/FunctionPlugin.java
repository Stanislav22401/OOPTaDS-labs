package plugin.functional;

/**
 * Functional plugin that transforms the XML document before save and after load (lab 5).
 */
public interface FunctionPlugin {
    String getName();

    String getVersion();

    /** Label shown in the Settings menu. */
    String getMenuLabel();

    /** Whether this processor is active when the plugin is first loaded. */
    boolean isEnabledByDefault();

    /**
     * Transforms canonical shapes XML immediately before writing to disk.
     */
    String processBeforeSave(String xml) throws Exception;

    /**
     * Transforms file XML immediately after reading from disk, before conversion to shapes.
     */
    String processAfterLoad(String xml) throws Exception;
}
