# Lab 4 — Plugins (Hierarchy Extension)

Extends **lab 3** (JSON serialization, shape editor) with **dynamic JAR plugins** that add new shape types without modifying host application code.

## Lab 3 features retained

- 6 built-in shapes, JSON save/load, property editing, add/delete.
- Variant **4**: JSON format.

## Lab 4 additions

| Requirement | Implementation |
|-------------|----------------|
| Dynamic module loading | `plugin.PluginLoader` + `URLClassLoader` + `ServiceLoader` |
| New class in hierarchy | Example: `Star` in `star-plugin.jar` |
| Work with new shape | Codec, renderer, property editor, mouse tool |
| UI for new shape | Toolbar button **Star** appears after plugin load |
| Load from folder | Auto-load `plugins/*.jar` on startup |
| Load single JAR | **Plugins → Load plugin JAR...** or CLI argument |

## Architecture

```
lab04/                    Host application (unchanged when adding plugins)
  plugin/                 Plugin API (ShapePlugin, PluginContext, PluginLoader)
  drawing/                MainFrame loads plugins, rebuilds toolbar
  plugins/                Drop compiled *.jar here
  plugins-src/star-plugin Sample plugin source

plugins-src/star-plugin/
  META-INF/services/plugin.ShapePlugin  → plugin.star.StarPlugin
  shapes/Star.java
  serialization/StarCodec.java
  rendering/StarRenderer.java
  editing/StarPropertyEditor.java
  tools/StarTool.java
```

Adding a new shape plugin:

1. Create a JAR with classes extending host APIs (`Shape`, `ShapeCodec`, `Tool`, …).
2. Implement `plugin.ShapePlugin` and register components in `register(PluginContext)`.
3. Add `META-INF/services/plugin.ShapePlugin` with the fully qualified plugin class name.
4. Place the JAR in `plugins/` or load via menu — **no host code changes**.

## Build

```bash
cd lab04
chmod +x build.sh
./build.sh
```

## Run

```bash
cd lab04
java drawing.MainFrame
```

Optional:

```bash
java -Dplugins.dir=/path/to/jars drawing.MainFrame
java drawing.MainFrame plugins/star-plugin.jar
```

## Usage

1. Start the app — `plugins/star-plugin.jar` is loaded automatically if present.
2. Use the **Star** tool on the toolbar (from plugin).
3. Save/load JSON including `"type":"Star"` objects.
4. **Plugins → Load plugin JAR...** to attach another plugin at runtime.

### Star JSON example

```json
{
  "shapes": [
    {"type": "Star", "centerX": 200, "centerY": 150, "outerRadius": 80}
  ]
}
```

## Smoke test (lab 3 + built-ins)

```bash
java drawing.SerializationSmokeTest
```
