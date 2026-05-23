#!/bin/bash
# Builds lab06 host app, shape plugins, functional plugins, and classmate (Adapter) plugins.
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
    local out_dir="$3"
    local build="$plugin_dir/build"
    rm -rf "$build"
    mkdir -p "$build"
    find "$plugin_dir" -name '*.java' > "$plugin_dir/sources.txt"
    javac -cp "$ROOT" -d "$build" @"$plugin_dir/sources.txt"
    cp -r "$plugin_dir/META-INF" "$build/"
    jar cf "$ROOT/$out_dir/$jar_name" -C "$build" .
    echo "  Plugin: $out_dir/$jar_name"
}

mkdir -p "$ROOT/plugins" "$ROOT/function-plugins" "$ROOT/classmate-plugins"

echo "Compiling shape plugins..."
build_shape_plugin "$ROOT/plugins-src/star-plugin" "star-plugin.jar"

echo "Compiling functional plugins (XSLT)..."
build_function_plugin "$ROOT/plugins-src/func-metadata" "metadata-xslt.jar" "function-plugins"
build_function_plugin "$ROOT/plugins-src/func-sort" "sort-shapes-xslt.jar" "function-plugins"
build_function_plugin "$ROOT/plugins-src/func-wrap" "wrap-drawing-xslt.jar" "function-plugins"

echo "Compiling classmate plugins (external API, loaded via Adapter)..."
build_function_plugin "$ROOT/plugins-src/classmate-header" "classmate-header.jar" "classmate-plugins"
build_function_plugin "$ROOT/plugins-src/classmate-uuid" "classmate-uuid.jar" "classmate-plugins"

echo "Done."
echo "  Run: java drawing.MainFrame"
