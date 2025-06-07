#!/bin/bash

# Define variables
SRC_DIR="src"
OUT_DIR="out/"

# Create output directory if it doesn't exist
mkdir -p "$OUT_DIR"

echo "Compiling all java files.."

# Compile all Java files inside SRC_DIR
javac -d "$OUT_DIR" $(find "$SRC_DIR" -name "*.java")

echo "Build complete! Classes are in $OUT_DIR"
