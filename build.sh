#!/bin/bash
set -e

# Step 1: Build fat jar
./gradlew shadowJar

# Step 2: Copy static/ into deploy/
rm -rf deploy/static
cp -r static deploy/static

echo "Build completed: deploy/deadzone-server.jar + static/"
