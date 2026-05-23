#!/bin/bash
# Builds lab05 host app, shape plugins, and functional (XSLT) plugins.
set -euo pipefail

ROOT="$(cd "$(dirname "$0")" && pwd)"
cd "$ROOT"

echo "Compiling main application..."
find . -name '*.java' ! -path './plugins-src/*' > sources.txt
javac @sources.txt

build_shape_plugin() {
    local plugin_dir="$1"
    local jar_name="$2"
    local build="$plugin_dir/build"
    rm -rf "$build"
    mkdir -p "$build"
    find "$plugin_dir" -name '*.java' > "$plugin_dir/sources.txt"
    javac -cp "$ROOT" -d "$build" @"$plugin_dir/sources.txt"
    cp -r "$plugin_dir/META-INF" "$build/"
    jar cf "$ROOT/plugins/$jar_name" -C "$build" .
    echo "  Shape plugin: plugins/$jar_name"
}

build_function_plugin() {
    local plugin_dir="$1"
    local jar_name="$2"
    local build="$plugin_dir/build"
    rm -rf "$build"
    mkdir -p "$build"
    find "$plugin_dir" -name '*.java' > "$plugin_dir/sources.txt"
    javac -cp "$ROOT" -d "$build" @"$plugin_dir/sources.txt"
    cp -r "$plugin_dir/META-INF" "$build/"
    jar cf "$ROOT/function-plugins/$jar_name" -C "$build" .
    echo "  Functional plugin: function-plugins/$jar_name"
}

mkdir -p "$ROOT/plugins" "$ROOT/function-plugins"

echo "Compiling shape plugins..."
build_shape_plugin "$ROOT/plugins-src/star-plugin" "star-plugin.jar"

echo "Compiling functional plugins (XSLT, variant 4)..."
build_function_plugin "$ROOT/plugins-src/func-metadata" "metadata-xslt.jar"
build_function_plugin "$ROOT/plugins-src/func-sort" "sort-shapes-xslt.jar"
build_function_plugin "$ROOT/plugins-src/func-wrap" "wrap-drawing-xslt.jar"

echo "Done."
echo "  Run: java drawing.MainFrame"
echo "  Shape plugins:     $ROOT/plugins/"
echo "  Functional plugins: $ROOT/function-plugins/"
