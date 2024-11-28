#!/bin/bash

# Exit on any failure
set -e

#PR_BODY="## Release notes
#         ### New
#         - Added the ability to copy payment method data to the clipboard
#         ### Fixed
#         - Fixed something
#         - Fixed something else
#         ## Here is another header
#         Some content"
#echo -e $PR_BODY
#ALLOWED_LABELS="Breaking changes,New,Fixed,Improved,Changed,Removed,Deprecated"

if [[ -z "$PR_BODY" ]]; then
  echo "PR_BODY is not provided. Please provide it in env list. Exiting..."
  exit 1
fi

if [[ -z "$ALLOWED_LABELS" ]]; then
  echo "ALLOWED_LABELS is not provided. Please provide it in env list. Exiting..."
  exit 1
fi

# Read allowed labels into an array
#mapfile -t ALLOWED_LABELS < "$ALLOWED_LABELS_FILE"
#ALLOWED_LABELS=$(cat $ALLOWED_LABELS_FILE)
#IFS=',' read -r -a ALLOWED_LABELS <<< "$ALLOWED_LABELS_LIST"
IFS=',' read -r -a LABELS <<< "$ALLOWED_LABELS"

# Function to check for valid release notes under a label
check_release_notes() {
  local label=$1
  local header="### $label"

  # Extract the content under the label
  label_content=$(echo "$PR_BODY" | awk -v header="$header" '
    $0 ~ header { capture = 1; next } # Start capturing after the specified header
    capture && /^[#]+[ ]/ { exit } # Stop at lines starting with one or more # followed by a space
    capture { print $0 } # Continue capturing until a stopping condition
  ')

  # Check if the content has at least one non-empty line
  if [[ ! -z $(echo "$label_content" | grep -vE '^[[:space:]]*$') ]]; then
    return 0 # Valid release notes found
  fi

  return 1 # No valid release notes found
}

# Loop through allowed labels and check for valid release notes
for label in "${LABELS[@]}"; do
  if check_release_notes "$label"; then
    echo "Valid release notes found under label: $label"
    exit 0
  fi
done

echo "Error: No valid release notes found in PR body. Please add release notes."
exit 1
