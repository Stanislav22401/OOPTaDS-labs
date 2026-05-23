# Lab 6 — Design Patterns

Extends **lab 5** with the **Adapter** pattern for classmate-style plugins and two additional patterns used in the host application.

**Подробное описание архитектуры, каталогов и файлов:** [ОПИСАНИЕ.md](ОПИСАНИЕ.md) (на русском).

## Lab 6 requirements

| Requirement | Implementation |
|-------------|----------------|
| Classmate functional plugin (instead of exchange) | `classmate-plugins/*.jar` with `classmate.api.XmlPipelinePlugin` |
| **Adapter** | `plugin.adapter.ClassmateFunctionPluginAdapter` → `FunctionPlugin` |
| **Pattern 2** | **Facade** — `drawing.persistence.DrawingPersistenceFacade` |
| **Pattern 3** | **Observer** — `ShapeList` + `ShapeListObserver` |
| **Pattern 4** | **Strategy** — `tools.ToolStrategyContext` + `Tool` |

## Adapter (classmate plugins)

Classmate modules use a **different API**:

```java
// classmate.api.XmlPipelinePlugin
String onExport(String xml);
String onImport(String xml);
```

The host only knows `plugin.functional.FunctionPlugin`.  
`ClassmatePluginLoader` loads JARs from `classmate-plugins/`, wraps each service with `ClassmateFunctionPluginAdapter`, and registers it in the same pipeline as native XSLT plugins.

### Sample classmate plugins

| JAR | Effect |
|-----|--------|
| `classmate-header.jar` | Prepends `<!-- Classmate Header Plugin -->` on export, strips on import |
| `classmate-uuid.jar` | Injects `<classmate-uuid …/>` inside `<shapes>` (off by default) |

Settings menu entries are marked **(classmate via Adapter)**.

## Other patterns (why they fit)

### Facade — `DrawingPersistenceFacade`

Hides JSON vs XML pipeline, `ProcessingShapeListSerializer`, and file extension rules from `MainFrame`. The UI calls `save` / `load` on one object.

### Observer — `ShapeList` / `ShapeListObserver`

When shapes are added, removed, or cleared, observers (list panel, canvas) refresh without tools calling UI code directly.

### Strategy — `ToolStrategyContext`

Each drawing tool (`LineTool`, `RectangleTool`, …) is a interchangeable strategy for mouse handling; the context swaps the active strategy when the user picks a toolbar button.

## Build

```bash
cd lab06
chmod +x build.sh
./build.sh
```

## Run

```bash
java drawing.MainFrame
```

## Tests

```bash
java drawing.SerializationSmokeTest
java drawing.FunctionPipelineSmokeTest
java drawing.AdapterPatternSmokeTest
```

## Directory layout

```
lab06/
  classmate/api/          # Stable API for classmate JARs
  plugin/adapter/         # Adapter implementations
  classmate-plugins/      # Built classmate JARs (auto-load)
  function-plugins/       # Native FunctionPlugin JARs
  plugins/                # Shape plugins
```

## Labs 3–5 retained

JSON model, shape plugins, XSLT functional pipeline, Settings toggles.
