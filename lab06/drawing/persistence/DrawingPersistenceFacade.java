package drawing.persistence;

import drawing.ShapeList;
import serialization.JsonShapeListSerializer;
import serialization.ProcessingShapeListSerializer;

import java.nio.file.Path;

/**
 * Facade pattern: single entry point for saving/loading drawings (JSON or XML pipeline).
 */
public class DrawingPersistenceFacade {
    private final JsonShapeListSerializer jsonSerializer;
    private final ProcessingShapeListSerializer processingSerializer;

    public DrawingPersistenceFacade(
            JsonShapeListSerializer jsonSerializer,
            ProcessingShapeListSerializer processingSerializer) {
        this.jsonSerializer = jsonSerializer;
        this.processingSerializer = processingSerializer;
    }

    public void save(ShapeList shapeList, Path file) throws Exception {
        if (isJsonPath(file)) {
            jsonSerializer.save(shapeList, file);
        } else {
            processingSerializer.save(shapeList, file);
        }
    }

    public void load(ShapeList shapeList, Path file) throws Exception {
        processingSerializer.load(shapeList, file);
    }

    private static boolean isJsonPath(Path file) {
        return file.getFileName().toString().toLowerCase().endsWith(".json");
    }
}
