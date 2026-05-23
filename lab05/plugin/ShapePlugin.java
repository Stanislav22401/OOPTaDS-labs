package plugin;

/**
 * Entry point for a dynamically loaded shape plugin JAR.
 * Implementations are discovered via {@code META-INF/services/plugin.ShapePlugin}.
 */
public interface ShapePlugin {
    /** Human-readable plugin name shown in the UI. */
    String getName();

    /** Optional version string. */
    String getVersion();

    /**
     * Registers codecs, renderers, property editors, and tools with the host application.
     */
    void register(PluginContext context);
}
