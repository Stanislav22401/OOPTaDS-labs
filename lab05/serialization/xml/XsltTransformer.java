package serialization.xml;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

/**
 * Applies an XSLT 1.0 stylesheet to an XML document string (lab 5, variant 4).
 */
public final class XsltTransformer {
    private XsltTransformer() {
    }

    public static String transform(String xml, String xslt) throws Exception {
        return transform(xml, xslt, Map.of());
    }

    public static String transform(String xml, String xslt, Map<String, String> parameters) throws Exception {
        TransformerFactory factory = TransformerFactory.newInstance();
        Source xsltSource = new StreamSource(new StringReader(xslt));
        Transformer transformer = factory.newTransformer(xsltSource);
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            transformer.setParameter(entry.getKey(), entry.getValue());
        }
        transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        StringWriter writer = new StringWriter();
        transformer.transform(
                new StreamSource(new StringReader(xml)),
                new StreamResult(writer));
        return writer.toString();
    }
}
