#!/bin/bash
rm -rf "*.class"
find -name "*.java" > sources.txt
# Compile the Java source file
javac @sources.txt
rm sources.txt
# Check if compilation was successful
if [ $? -eq 0 ]; then
    # Run the Checkers game
    java CheckersGameRunner
else
    echo "Compilation failed. Please check the Java source code."
fi