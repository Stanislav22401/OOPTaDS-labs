package plugin.functional.metadata;

import plugin.functional.FunctionPlugin;
import serialization.xml.XsltTransformer;

import java.time.Instant;
import java.util.Map;

/**
 * Adds a {@code metadata} element before save and removes it after load using XSLT.
 */
public class MetadataXsltPlugin implements FunctionPlugin {
    private static final String ADD_METADATA_XSLT =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n"
            + "  <xsl:output method=\"xml\" indent=\"yes\"/>\n"
            + "  <xsl:param name=\"savedAt\"/>\n"
            + "  <xsl:template match=\"@*|node()\">\n"
            + "    <xsl:copy>\n"
            + "      <xsl:apply-templates select=\"@*|node()\"/>\n"
            + "    </xsl:copy>\n"
            + "  </xsl:template>\n"
            + "  <xsl:template match=\"shapes\">\n"
            + "    <shapes>\n"
            + "      <metadata savedAt=\"{$savedAt}\"/>\n"
            + "      <xsl:apply-templates select=\"shape\"/>\n"
            + "    </shapes>\n"
            + "  </xsl:template>\n"
            + "</xsl:stylesheet>";

    private static final String REMOVE_METADATA_XSLT =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n"
            + "  <xsl:output method=\"xml\" indent=\"yes\"/>\n"
            + "  <xsl:template match=\"metadata\"/>\n"
            + "  <xsl:template match=\"@*|node()\">\n"
            + "    <xsl:copy>\n"
            + "      <xsl:apply-templates select=\"@*|node()\"/>\n"
            + "    </xsl:copy>\n"
            + "  </xsl:template>\n"
            + "</xsl:stylesheet>";

    @Override
    public String getName() {
        return "Metadata XSLT Plugin";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String getMenuLabel() {
        return "Add save metadata (XSLT)";
    }

    @Override
    public boolean isEnabledByDefault() {
        return true;
    }

    @Override
    public String processBeforeSave(String xml) throws Exception {
        return XsltTransformer.transform(xml, ADD_METADATA_XSLT, Map.of("savedAt", Instant.now().toString()));
    }

    @Override
    public String processAfterLoad(String xml) throws Exception {
        return XsltTransformer.transform(xml, REMOVE_METADATA_XSLT);
    }
}
