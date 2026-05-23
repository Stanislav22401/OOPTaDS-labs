package serialization;

import drawing.ShapeList;
import plugin.functional.FunctionPluginRegistry;
import serialization.xml.JsonXmlBridge;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Saves/loads shapes through XML with optional functional plugin pipeline (lab 5, variant 4 — XSLT).
 */
public class ProcessingShapeListSerializer {
    private final JsonShapeListSerializer jsonSerializer;
    private final FunctionPluginRegistry functionPlugins;

    public ProcessingShapeListSerializer(
            JsonShapeListSerializer jsonSerializer,
            FunctionPluginRegistry functionPlugins) {
        this.jsonSerializer = jsonSerializer;
        this.functionPlugins = functionPlugins;
    }

    /**
     * Converts shapes to XML, runs enabled before-save processors, and writes the file.
     */
    public void save(ShapeList shapeList, Path file) throws Exception {
        String json = jsonSerializer.toJson(shapeList);
        String xml = JsonXmlBridge.jsonToXml(json);
        if (functionPlugins.hasEnabledPlugins()) {
            xml = functionPlugins.applyBeforeSave(xml);
        }
        Files.writeString(file, xml, StandardCharsets.UTF_8);
    }

    /**
     * Reads XML from disk, runs after-load processors, and restores shapes.
     */
    public void load(ShapeList shapeList, Path file) throws Exception {
        String content = Files.readString(file, StandardCharsets.UTF_8).trim();
        String json;
        if (content.startsWith("{")) {
            json = content;
        } else {
            String xml = content;
            if (functionPlugins.hasEnabledPlugins()) {
                xml = functionPlugins.applyAfterLoad(xml);
            }
            json = JsonXmlBridge.xmlToJson(xml);
        }
        jsonSerializer.fromJson(shapeList, json);
    }
}
