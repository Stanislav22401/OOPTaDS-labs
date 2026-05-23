package classmate.plugins;

import classmate.api.XmlPipelinePlugin;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Second classmate-style plugin: adds a unique document tag on export (different processing rules).
 */
public class UuidTagXmlPipeline implements XmlPipelinePlugin {
    private static final Pattern TAG_PATTERN =
            Pattern.compile("<classmate-uuid[^>]*>[^<]*</classmate-uuid>\\s*", Pattern.DOTALL);

    @Override
    public String getPluginTitle() {
        return "UUID Tag Pipeline";
    }

    @Override
    public String getPluginVersion() {
        return "1.0";
    }

    @Override
    public String getSettingsLabel() {
        return "Inject document UUID tag (classmate plugin)";
    }

    @Override
    public boolean isActiveByDefault() {
        return false;
    }

    @Override
    public String onExport(String xml) throws Exception {
        String id = UUID.randomUUID().toString();
        String tag = "<classmate-uuid id=\"" + id + "\">" + id + "</classmate-uuid>\n";
        if (xml.contains("<shapes")) {
            return xml.replaceFirst("<shapes>", "<shapes>\n  " + tag);
        }
        return tag + xml;
    }

    @Override
    public String onImport(String xml) throws Exception {
        Matcher matcher = TAG_PATTERN.matcher(xml);
        return matcher.replaceAll("");
    }
}
