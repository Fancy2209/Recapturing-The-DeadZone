#!/bin/bash
set -e

VERSION=$(date +"%Y.%m.%d.%H%M%S")
RELEASE_NAME="release-$VERSION"

# Zip the deploy folder
ZIP_NAME="deploy-$VERSION.zip"
cd deploy
zip -r "../$ZIP_NAME" .
cd ..

# Create GitHub release using gh CLI
gh release create "$RELEASE_NAME" "$ZIP_NAME" \
  --title "$RELEASE_NAME" \
  --notes-file RELEASE_NOTES.md

echo "GitHub release $RELEASE_NAME published with $ZIP_NAME"
