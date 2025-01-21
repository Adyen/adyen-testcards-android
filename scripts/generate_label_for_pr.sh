#!/bin/bash

# Input string
input_string="feature/new-feature"

function generate_label_for_pr() {
  if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <branch_name>" >&2
    exit 1
  fi
  branch_name=$1
  # Check if the string starts with a specific prefix
  if [[ "branch_name" == feature/* ]]; then
    label="Feature"
  elif [[ "branch_name" == fix/* ]]; then
    label="Fix"
  elif [[ "branch_name" == chore/* ]]; then
    label="Chore"
  else
    echo "Branch name is not valid. It should start with feature/, fix/, chore/ or renovate/" >&2
    exit 1
  fi

  echo "$label"
}

generate_label_for_pr $1
