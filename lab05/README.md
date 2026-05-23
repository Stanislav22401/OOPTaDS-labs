# Lab 5 — Functional Plugins (Mandatory, Variant 4: XML / XSLT)

Extends **lab 4** with **functional plugins** that transform the document **before save** and **after load**.  
Variant **4**: **XML transformation using XSLT**.

## Pipeline

```
Save:  ShapeList → JSON → XML → [enabled XSLT plugins] → shapes.xml
Load:  shapes.xml → [reverse XSLT plugins] → XML → JSON → ShapeList
```

Legacy **JSON** files (`.json`) load without the XML pipeline.

## Functional plugins (3 samples)

| JAR | Settings menu | Before save | After load |
|-----|---------------|-------------|------------|
| `metadata-xslt.jar` | Add save metadata (XSLT) | Inserts `<metadata savedAt="…"/>` | Removes metadata |
| `sort-shapes-xslt.jar` | Sort shapes by type (XSLT) | Sorts `<shape>` by `@type` | No-op |
| `wrap-drawing-xslt.jar` | Wrap in drawing root (XSLT) | Wraps in `<drawing version="1">` | Unwraps (off by default) |

Plugins live in `function-plugins/` (auto-loaded). Shape plugins remain in `plugins/`.

## Settings menu

**Settings** lists checkboxes for each loaded functional plugin.  
Save/load uses only **enabled** processors.

## Build

```bash
cd lab05
chmod +x build.sh
./build.sh
```

## Run

```bash
java drawing.MainFrame
```

## Tests

```bash
java drawing.SerializationSmokeTest      # JSON built-ins
java drawing.FunctionPipelineSmokeTest # XML + XSLT plugins
```

## Adding a functional plugin

1. Implement `plugin.functional.FunctionPlugin` (`processBeforeSave` / `processAfterLoad` with XSLT).
2. Register in `META-INF/services/plugin.functional.FunctionPlugin`.
3. Build JAR with `-cp` pointing at compiled lab05 classes.
4. Copy to `function-plugins/` — no host code changes.

## Labs 3–4 features retained

- JSON shape model (lab 3, variant 4).
- Dynamic **shape** plugins (lab 4, e.g. Star).
