#!/bin/bash
set -euo pipefail

SDK_ROOT=/usr/local/lib/android/sdk
CMDLINE_TOOLS_ZIP=/tmp/cmdline-tools.zip

echo ">>> Installing Android SDK command-line tools..."
mkdir -p "$SDK_ROOT/cmdline-tools"

wget -q https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip \
  -O "$CMDLINE_TOOLS_ZIP"

unzip -q "$CMDLINE_TOOLS_ZIP" -d "$SDK_ROOT/cmdline-tools"
mv "$SDK_ROOT/cmdline-tools/cmdline-tools" "$SDK_ROOT/cmdline-tools/latest"
rm "$CMDLINE_TOOLS_ZIP"

echo ">>> Accepting licenses..."
sdkmanager --sdk_root="$SDK_ROOT" --licenses <<< $(yes)

echo ">>> Installing SDK packages..."
sdkmanager --sdk_root="$SDK_ROOT" \
  "platform-tools" \
  "platforms;android-29" \
  "build-tools;34.0.0"

echo ">>> Android SDK setup complete."
