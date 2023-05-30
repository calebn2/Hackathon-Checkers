#!/bin/bash
rm -rf "*.class"
# Compile the Java source file
javac CheckersGame.java
javac CheckersGameRunner.java
# Check if compilation was successful
if [ $? -eq 0 ]; then
    # Run the Checkers game
    java CheckersGameRunner
else
    echo "Compilation failed. Please check the Java source code."
fi