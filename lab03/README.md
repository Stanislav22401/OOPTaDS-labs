# Lab 3 — Object Serialization (Variant 4: JSON)

Graphics editor with **JSON** serialization/deserialization, property editing, and add/remove operations.  
Built on the shape hierarchy from lab 2 (6 shape types, extensible design).

## Variant

| Variant | Format |
|---------|--------|
| **4**   | **JSON** |

## Requirements covered

- Hierarchy of **6+ classes**: `Line`, `Rectangle`, `Ellipse`, `Triangle`, `Square`, `Circle` (+ abstract `Shape`).
- **JSON** save/load via `File` menu.
- UI: **edit properties**, **add** (drawing tools), **delete** (list panel), **serialize/deserialize**.
- Extensibility: new shape types register a codec, property editor, renderer, and tool in `ApplicationBootstrap` only — no changes to serializer or UI dispatch logic.
- No `switch`/`case`, chained `if-else` for type dispatch, or **reflection**.

## JSON format example

```json
{
  "shapes": [
    {"type":"Line","x1":10,"y1":20,"x2":100,"y2":80},
    {"type":"Rectangle","x":50,"y":50,"width":120,"height":60},
    {"type":"Square","x":200,"y":100,"side":80},
    {"type":"Circle","x":300,"y":200,"diameter":90},
    {"type":"Ellipse","x":400,"y":50,"width":100,"height":50},
    {"type":"Triangle","xPoints":[10,60,30],"yPoints":[200,200,120]}
  ]
}
```

## Packages

| Package | Role |
|---------|------|
| `shapes` | Mutable shape data |
| `rendering` | Draw shapes (`RendererRegistry`) |
| `tools` | Mouse tools to create shapes |
| `serialization` | JSON codecs and `JsonShapeListSerializer` |
| `serialization.json` | Lightweight JSON parser/writer |
| `editing` | Property editors for the edit dialog |
| `drawing` | Swing UI (`MainFrame`) |

## Build and run

```bash
cd lab03
find . -name "*.java" > sources.txt
javac @sources.txt
java drawing.MainFrame
```

Requires **Java 11+** (uses `Files.readString` / `writeString`).

## Usage

1. Select a tool on the toolbar and draw on the canvas (triangle: three clicks).
2. Select a shape in the left list → **Edit properties** or **Delete selected**.
3. **File → Save to JSON...** / **Load from JSON...**
