package plugin.functional.wrap;

import plugin.functional.FunctionPlugin;
import serialization.xml.XsltTransformer;

/**
 * Wraps the document in a {@code drawing} root element before save and unwraps after load (XSLT).
 */
public class WrapDrawingXsltPlugin implements FunctionPlugin {
    private static final String WRAP_XSLT =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n"
            + "  <xsl:output method=\"xml\" indent=\"yes\"/>\n"
            + "  <xsl:template match=\"/\">\n"
            + "    <drawing version=\"1\">\n"
            + "      <xsl:copy-of select=\"shapes\"/>\n"
            + "    </drawing>\n"
            + "  </xsl:template>\n"
            + "</xsl:stylesheet>";

    private static final String UNWRAP_XSLT =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n"
            + "  <xsl:output method=\"xml\" indent=\"yes\"/>\n"
            + "  <xsl:template match=\"drawing\">\n"
            + "    <xsl:copy-of select=\"shapes\"/>\n"
            + "  </xsl:template>\n"
            + "</xsl:stylesheet>";

    @Override
    public String getName() {
        return "Wrap Drawing XSLT Plugin";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String getMenuLabel() {
        return "Wrap in drawing root (XSLT)";
    }

    @Override
    public boolean isEnabledByDefault() {
        return false;
    }

    @Override
    public String processBeforeSave(String xml) throws Exception {
        return XsltTransformer.transform(xml, WRAP_XSLT);
    }

    @Override
    public String processAfterLoad(String xml) throws Exception {
        return XsltTransformer.transform(xml, UNWRAP_XSLT);
    }
}
