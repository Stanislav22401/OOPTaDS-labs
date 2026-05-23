# Simple Graphics Editor

A primitive graphics editor built in Java (Swing) that allows creating various shapes using mouse input.  
This project demonstrates an extensible object-oriented design where new shape types can be added without modifying existing code.

## Features

- **Six shape types**: Line, Rectangle, Ellipse, Triangle, Square, Circle.
- **Mouse‑driven creation**: click and drag for most shapes; triangle uses three consecutive clicks.
- **Clean separation of concerns**:
  - `shapes` – data‑only classes (no drawing logic).
  - `rendering` – separate renderers for each shape.
  - `tools` – mouse interaction logic for shape creation.
  - `drawing` – canvas, shape container, and main window.
- **Extensibility**: adding a new shape requires only a new shape class, a renderer (or reuse of an existing one), and a tool – no changes to drawing or event handling code.
- **Full English comments** explaining every major class and method.

## Architecture

The project is organised into four packages:

### `shapes`
Contains the abstract base class `Shape` and concrete shape classes:
- `Line`
- `Rectangle`
- `Ellipse`
- `Triangle`
- `Square` (inherits from `Rectangle`)
- `Circle` (inherits from `Ellipse`)

Each shape holds only its geometric data (e.g., coordinates, dimensions) – **no drawing code**.

### `rendering`
Provides the `ShapeRenderer` interface and implementations for each shape:
- `LineRenderer`
- `RectangleRenderer`
- `EllipseRenderer`
- `TriangleRenderer`

`Square` and `Circle` reuse the renderers of their parents (`RectangleRenderer` and `EllipseRenderer` respectively).  
Renderers are selected dynamically at draw time via a `Map<Class<? extends Shape>, ShapeRenderer>`.

### `tools`
Defines the `Tool` interface and one tool per shape:
- `LineTool`
- `RectangleTool`
- `EllipseTool`
- `TriangleTool`
- `SquareTool`
- `CircleTool`

Tools process mouse events (`mousePressed`, `mouseDragged`, `mouseReleased`) and produce a finished shape, which is added to the `ShapeList`. They also draw a preview (rubber‑band effect) while the shape is being created.

### `drawing`
Contains:
- `ShapeList` – a simple container for all shapes.
- `DrawingPanel` – the canvas; listens to mouse events, forwards them to the active tool, and paints both finished shapes (via renderers) and the current preview.
- `MainFrame` – the main window with a toolbar to select tools. It also registers the renderers in a map and passes it to the drawing panel.

## How It Works

1. **Startup**: `MainFrame` creates a `DrawingPanel`, registers renderers for each shape class, and builds a toolbar with buttons for all tools.
2. **Tool selection**: clicking a toolbar button sets the corresponding tool as the active tool in the drawing panel.
3. **Shape creation**:
   - The user interacts with the canvas using the mouse.
   - The `DrawingPanel` forwards mouse events to the active tool.
   - The tool updates its internal state (e.g., start point, current point) and calls `repaint()` to show a preview.
   - When the mouse is released (or after the third click for triangle), the tool instantiates the appropriate shape and adds it to the `ShapeList`.
4. **Rendering**: On every `paintComponent` call, the panel iterates over all shapes in the `ShapeList`, looks up the correct renderer from the map, and calls its `render` method. Then it asks the active tool to draw its preview.

## Adding a New Shape

To introduce a new shape (e.g., `Polygon`) follow these steps – **no existing code needs to be modified**:

1. Create a new class in the `shapes` package that extends `Shape` and holds the necessary data.
2. If the shape requires a special drawing routine, create a new class in `rendering` that implements `ShapeRenderer`. Otherwise, you can reuse an existing renderer.
3. Create a new class in `tools` that implements `Tool` and handles mouse input to produce your shape.
4. In `MainFrame`:
   - Register the new shape class with its renderer in the `renderers` map.
   - Add a button to the toolbar that sets the new tool.

The rest of the system (drawing loop, event handling) works automatically.

## Requirements & Running

- **OS**: Debian 12 (or any system with Java 8+).
- **Java**: OpenJDK 11 or newer (tested with OpenJDK 17).

### Compile and run

```bash```
# Install OpenJDK if not already present
sudo apt update
sudo apt install openjdk-17-jdk

# Navigate to the project root (where the packages are located)
cd /path/to/project

# Compile all sources
javac drawing/MainFrame.java shapes/*.java rendering/*.java tools/*.java

# Run the editor
java drawing.MainFrame