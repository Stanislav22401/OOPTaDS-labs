package classmate.plugins;

import classmate.api.XmlPipelinePlugin;

/**
 * Classmate plugin: prepends an XML comment on export and strips it on import.
 * Uses the external {@link XmlPipelinePlugin} API (integrated via Adapter in the host).
 */
public class HeaderCommentXmlPipeline implements XmlPipelinePlugin {
    private static final String MARKER = "<!-- Classmate Header Plugin -->";

    @Override
    public String getPluginTitle() {
        return "Header Comment Pipeline";
    }

    @Override
    public String getPluginVersion() {
        return "1.0";
    }

    @Override
    public String getSettingsLabel() {
        return "XML header comment (classmate plugin)";
    }

    @Override
    public boolean isActiveByDefault() {
        return true;
    }

    @Override
    public String onExport(String xml) throws Exception {
        return MARKER + "\n" + xml;
    }

    @Override
    public String onImport(String xml) throws Exception {
        String trimmed = xml.trim();
        if (trimmed.startsWith(MARKER)) {
            return trimmed.substring(MARKER.length()).trim();
        }
        return xml;
    }
}
