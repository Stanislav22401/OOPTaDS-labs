package serialization.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import serialization.json.JsonValue;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Converts between the internal JSON shape list format and canonical XML for XSLT pipelines.
 */
public final class JsonXmlBridge {
    private JsonXmlBridge() {
    }

    public static String jsonToXml(String json) throws Exception {
        Object parsed = JsonValue.parse(json);
        Map<String, Object> root = JsonValue.asObject(parsed);
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element shapesElement = document.createElement("shapes");
        document.appendChild(shapesElement);

        List<Object> items = JsonValue.asArray(root.get("shapes"));
        for (Object item : items) {
            Map<String, Object> shapeMap = JsonValue.asObject(item);
            String type = JsonValue.asString(shapeMap.get("type"));
            Element shapeElement = document.createElement("shape");
            shapeElement.setAttribute("type", type);
            shapesElement.appendChild(shapeElement);

            for (Map.Entry<String, Object> entry : shapeMap.entrySet()) {
                if ("type".equals(entry.getKey())) {
                    continue;
                }
                appendValue(document, shapeElement, entry.getKey(), entry.getValue());
            }
        }
        return documentToString(document);
    }

    public static String xmlToJson(String xml) throws Exception {
        Document document = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(new org.xml.sax.InputSource(new StringReader(xml)));
        Element shapesRoot = locateShapesElement(document);

        List<Object> shapes = new ArrayList<>();
        NodeList shapeNodes = shapesRoot.getChildNodes();
        for (int i = 0; i < shapeNodes.getLength(); i++) {
            if (shapeNodes.item(i).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            Element shapeElement = (Element) shapeNodes.item(i);
            if (!"shape".equals(shapeElement.getNodeName())) {
                continue;
            }
            Map<String, Object> shapeMap = new LinkedHashMap<>();
            shapeMap.put("type", shapeElement.getAttribute("type"));
            NodeList children = shapeElement.getChildNodes();
            for (int c = 0; c < children.getLength(); c++) {
                Node child = children.item(c);
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    readProperty(shapeMap, (Element) child);
                }
            }
            shapes.add(shapeMap);
        }

        Map<String, Object> jsonRoot = new LinkedHashMap<>();
        jsonRoot.put("shapes", shapes);
        return JsonValue.stringify(jsonRoot);
    }

    private static Element locateShapesElement(Document document) {
        Element root = document.getDocumentElement();
        if ("shapes".equals(root.getNodeName())) {
            return root;
        }
        NodeList list = document.getElementsByTagName("shapes");
        if (list.getLength() == 0) {
            throw new IllegalArgumentException("No <shapes> element in XML");
        }
        return (Element) list.item(0);
    }

    private static void appendValue(Document document, Element parent, String name, Object value) {
        if (value instanceof List) {
            Element listElement = document.createElement(name);
            parent.appendChild(listElement);
            for (Object item : (List<?>) value) {
                Element valueElement = document.createElement("value");
                valueElement.setTextContent(String.valueOf(item));
                listElement.appendChild(valueElement);
            }
            return;
        }
        Element property = document.createElement(name);
        property.setTextContent(String.valueOf(value));
        parent.appendChild(property);
    }

    private static void readProperty(Map<String, Object> shapeMap, Element element) {
        String name = element.getNodeName();
        if ("metadata".equals(name)) {
            return;
        }
        NodeList values = element.getElementsByTagName("value");
        if (values.getLength() > 0 && element.getChildNodes().getLength() == values.getLength()) {
            List<Object> list = new ArrayList<>();
            for (int i = 0; i < values.getLength(); i++) {
                list.add(parseNumber(values.item(i).getTextContent()));
            }
            shapeMap.put(name, list);
            return;
        }
        shapeMap.put(name, parseNumber(element.getTextContent().trim()));
    }

    private static Object parseNumber(String text) {
        if (text.contains(".")) {
            return Double.parseDouble(text);
        }
        return Integer.parseInt(text);
    }

    private static String documentToString(Document document) throws Exception {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(writer));
        return writer.toString();
    }
}
