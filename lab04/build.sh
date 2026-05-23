#!/bin/bash
# Builds the lab04 application and sample star plugin JAR.
set -euo pipefail

ROOT="$(cd "$(dirname "$0")" && pwd)"
cd "$ROOT"

echo "Compiling main application..."
find . -name '*.java' ! -path './plugins-src/*' > sources.txt
javac @sources.txt

echo "Compiling star plugin..."
PLUGIN_ROOT="$ROOT/plugins-src/star-plugin"
BUILD="$PLUGIN_ROOT/build"
rm -rf "$BUILD"
mkdir -p "$BUILD"

find "$PLUGIN_ROOT" -name '*.java' > "$PLUGIN_ROOT/sources.txt"
javac -cp "$ROOT" -d "$BUILD" @"$PLUGIN_ROOT/sources.txt"
cp -r "$PLUGIN_ROOT/META-INF" "$BUILD/"

mkdir -p "$ROOT/plugins"
jar cf "$ROOT/plugins/star-plugin.jar" -C "$BUILD" .

echo "Done."
echo "  Run app:    java drawing.MainFrame"
echo "  Plugin JAR: $ROOT/plugins/star-plugin.jar"
