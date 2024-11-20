#!/bin/bash

# Fetch allowed labels from the environment variable
read_allowed_labels() {
    IFS=',' read -r -a ALLOWED_LABELS_ARRAY <<< "$ALLOWED_LABELS"
    echo "${ALLOWED_LABELS_ARRAY[@]}"
}

# Fetch recent commits since the last release tag
fetch_recent_commits() {
    LATEST_TAG=$(git describe --tags --abbrev=0)
    COMMITS=$(git log --oneline "$LATEST_TAG"..HEAD)
    echo "$LATEST_TAG"
    echo "$COMMITS"
}

# Fetch PR body using GitHub API
fetch_pr_body() {
    local PR_NUMBER=$1
    API_URL="https://api.github.com/repos/$GITHUB_REPO/pulls/$PR_NUMBER"
    PR_RESPONSE=$(curl -s -H "Authorization: token $GITHUB_TOKEN" "$API_URL")
    echo -e "$PR_RESPONSE" | jq -r '.body'
}

# Extract label content from PR body
extract_label_content() {
    local PR_BODY=$1
    local HEADER=$2
    echo "$PR_BODY" | awk -v header="$HEADER" '
        $0 ~ header { capture = 1; next } # Start capturing after the specified header
        capture && /^[#]+[ ]/ { exit } # Stop at lines starting with one or more # followed by a space
        capture { print $0 } # Continue capturing until a stopping condition
    '
}

# Generate release notes from commits
generate_release_notes_from_commits() {
    local COMMITS=$1

    # Initialize variables to hold content for each label
    IFS=',' read -r -a LABELS <<< "$ALLOWED_LABELS"
    declare -A LABEL_CONTENTS
    for LABEL in "${LABELS[@]}"; do
        LABEL_CONTENTS["$LABEL"]=""
    done

    # Iterate over each commit to find PR numbers, fetch PR body and extract release notes
    while IFS= read -r COMMIT; do
        if [[ $COMMIT =~ Merge\ pull\ request\ \#([0-9]+) ]]; then
            PR_NUMBER="${BASH_REMATCH[1]}"
            PR_BODY=$(fetch_pr_body "$PR_NUMBER")

            for LABEL in "${LABELS[@]}"; do
                HEADER="### $LABEL"
                LABEL_CONTENT=$(extract_label_content "$PR_BODY" "$HEADER")
                if [ -n "$LABEL_CONTENT" ]; then
                    LABEL_CONTENTS["$LABEL"]="${LABEL_CONTENTS[$LABEL]}$LABEL_CONTENT\n"
                fi
            done
        fi
    done <<< "$COMMITS"

    # Combine notes by labels
    OUTPUT=""
    for LABEL in "${LABELS[@]}"; do
        if [ -n "${LABEL_CONTENTS[$LABEL]}" ]; then
            OUTPUT="${OUTPUT}### $LABEL\n${LABEL_CONTENTS[$LABEL]}\n"
        fi
    done

    echo "$OUTPUT"
}

# Save release notes to a file
save_release_notes() {
    local OUTPUT=$1
    local FILENAME=$2
    if [ -n "$OUTPUT" ]; then
        printf "%s" "$OUTPUT" >> "$FILENAME"
        printf "%s" "$OUTPUT" >> "$GITHUB_STEP_SUMMARY"
    fi
}

# Main Execution
generate_release_notes() {
    if [ -z "$GITHUB_TOKEN" ]; then
        echo "GITHUB_TOKEN is not provided. Please provide it in env list. Exiting..."
        exit 1
    fi

    if [ -z "$GITHUB_REPO" ]; then
        echo "GITHUB_REPO is not provided. Please provide it in env list. Exiting..."
        exit 1
    fi

    echo -e "Allowed labels: $ALLOWED_LABELS"
    echo -e "Release notes file name: $RELEASE_NOTES_FILE_NAME"

    read -r LATEST_TAG COMMITS <<< "$(fetch_recent_commits)"
    echo -e "Latest tag: $LATEST_TAG"
    echo -e "Commits since latest tag:\n$COMMITS"

    LATEST_TAG=$(git describe --tags --abbrev=0)
    COMMITS=$(git log --oneline "$LATEST_TAG"..HEAD)
    echo -e "Commits since latest tag:\n$COMMITS"

    RELEASE_NOTES=$(generate_release_notes_from_commits "$COMMITS")
    echo -e "Generated release notes:\n$RELEASE_NOTES"

    save_release_notes "$RELEASE_NOTES" "$RELEASE_NOTES_FILE_NAME"
}

ALLOWED_LABELS=$1
RELEASE_NOTES_FILE_NAME=$2

generate_release_notes
