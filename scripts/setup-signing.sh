#!/usr/bin/env bash
# setup-signing.sh — Generate Android release keystore and upload secrets to GitHub
# Run once before the first release. Requires: keytool (JDK), gh CLI authenticated.
set -euo pipefail

REPO="cocodedk/lifemeter"
KEYSTORE_FILE="release.keystore"
KEYSTORE_DIR="$(dirname "$0")/../app"  # matches workflow: $GITHUB_WORKSPACE/app/release.keystore

echo "=== Android Release Signing Setup ==="
echo "Repo: $REPO"
echo ""

# 1. Check prerequisites
command -v keytool >/dev/null 2>&1 || { echo "ERROR: keytool not found. Install JDK."; exit 1; }
command -v gh >/dev/null 2>&1 || { echo "ERROR: gh CLI not found."; exit 1; }
gh auth status >/dev/null 2>&1 || { echo "ERROR: gh CLI not authenticated. Run: gh auth login"; exit 1; }

# 2. Collect signing parameters
echo "Enter keystore details (no quotes in passwords):"
read -rp "Key alias [release]: " KEY_ALIAS
KEY_ALIAS="${KEY_ALIAS:-release}"

read -rsp "Keystore password: " KEYSTORE_PASSWORD; echo
if [[ "$KEYSTORE_PASSWORD" == *"'"* ]] || [[ "$KEYSTORE_PASSWORD" == *'"'* ]]; then
  echo "ERROR: Passwords must not contain quotes."; exit 1
fi

read -rsp "Key password (enter to use same as keystore): " KEY_PASSWORD; echo
KEY_PASSWORD="${KEY_PASSWORD:-$KEYSTORE_PASSWORD}"
if [[ "$KEY_PASSWORD" == *"'"* ]] || [[ "$KEY_PASSWORD" == *'"'* ]]; then
  echo "ERROR: Passwords must not contain quotes."; exit 1
fi

read -rp "Your name (for certificate) [Babak Bandpey]: " CERT_NAME
CERT_NAME="${CERT_NAME:-Babak Bandpey}"

read -rp "Organisation [Cocode]: " CERT_ORG
CERT_ORG="${CERT_ORG:-Cocode}"

read -rp "Country code [DK]: " CERT_COUNTRY
CERT_COUNTRY="${CERT_COUNTRY:-DK}"

# 3. Generate or reuse keystore
if [[ -f "$KEYSTORE_DIR/$KEYSTORE_FILE" ]]; then
  echo "Keystore already exists at $KEYSTORE_DIR/$KEYSTORE_FILE — reusing."
else
  echo "Generating new keystore..."
  mkdir -p "$KEYSTORE_DIR"
  keytool -genkeypair \
    -keystore "$KEYSTORE_DIR/$KEYSTORE_FILE" \
    -alias "$KEY_ALIAS" \
    -keyalg RSA -keysize 2048 -validity 10000 \
    -storepass "$KEYSTORE_PASSWORD" \
    -keypass "$KEY_PASSWORD" \
    -dname "CN=$CERT_NAME, O=$CERT_ORG, C=$CERT_COUNTRY"
  echo "Keystore generated."
fi

# 4. Verify keystore
keytool -list \
  -keystore "$KEYSTORE_DIR/$KEYSTORE_FILE" \
  -alias "$KEY_ALIAS" \
  -storepass "$KEYSTORE_PASSWORD" \
  -keypass "$KEY_PASSWORD" >/dev/null 2>&1 || {
    echo "ERROR: Keystore verification failed. Check passwords and alias."; exit 1
  }
echo "Keystore verified."

# 5. Encode keystore to base64
KEYSTORE_BASE64=$(base64 -w 0 "$KEYSTORE_DIR/$KEYSTORE_FILE")

# 6. Upload secrets to GitHub
echo ""
echo "Uploading secrets to $REPO..."

gh secret set KEYSTORE_BASE64   --body "$KEYSTORE_BASE64"   --repo "$REPO"
gh secret set KEYSTORE_PASSWORD --body "$KEYSTORE_PASSWORD" --repo "$REPO"
gh secret set KEY_ALIAS         --body "$KEY_ALIAS"         --repo "$REPO"
gh secret set KEY_PASSWORD      --body "$KEY_PASSWORD"      --repo "$REPO"

echo ""
echo "=== Done! ==="
echo "4 secrets uploaded to GitHub. You can now trigger a release workflow."
echo "IMPORTANT: Back up $KEYSTORE_DIR/$KEYSTORE_FILE — losing it means you cannot update the app on Play Store."
echo "IMPORTANT: Never commit the keystore file to git."
