package plugin.functional.sort;

import plugin.functional.FunctionPlugin;
import serialization.xml.XsltTransformer;

/**
 * Sorts {@code shape} elements by their {@code type} attribute before save (XSLT).
 */
public class SortShapesXsltPlugin implements FunctionPlugin {
    private static final String SORT_XSLT =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n"
            + "  <xsl:output method=\"xml\" indent=\"yes\"/>\n"
            + "  <xsl:template match=\"shapes\">\n"
            + "    <shapes>\n"
            + "      <xsl:apply-templates select=\"metadata\"/>\n"
            + "      <xsl:for-each select=\"shape\">\n"
            + "        <xsl:sort select=\"@type\"/>\n"
            + "        <xsl:copy-of select=\".\"/>\n"
            + "      </xsl:for-each>\n"
            + "    </shapes>\n"
            + "  </xsl:template>\n"
            + "  <xsl:template match=\"@*|node()\">\n"
            + "    <xsl:copy>\n"
            + "      <xsl:apply-templates select=\"@*|node()\"/>\n"
            + "    </xsl:copy>\n"
            + "  </xsl:template>\n"
            + "</xsl:stylesheet>";

    @Override
    public String getName() {
        return "Sort Shapes XSLT Plugin";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String getMenuLabel() {
        return "Sort shapes by type (XSLT)";
    }

    @Override
    public boolean isEnabledByDefault() {
        return true;
    }

    @Override
    public String processBeforeSave(String xml) throws Exception {
        return XsltTransformer.transform(xml, SORT_XSLT);
    }

    @Override
    public String processAfterLoad(String xml) throws Exception {
        return xml;
    }
}
