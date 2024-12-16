#!/bin/bash
#
# Copyright (c) 2024 Adyen N.V.
#
# This file is open source and available under the MIT license. See the LICENSE file for more info.
#
# Created by ozgur on 10/12/2024.
#
function fetch_updated_release_notes_file() {
    if [ "$#" -ne 1 ]; then
        echo "Usage: $0 <project_root_directory>"
        exit 1
    fi

    directory=$1
    # Regex for versioned files (starts with a version number like 1.2.3.md)
    version_name_regex="^[0-9]{1,2}\.[0-9]{1,2}\.[0-9]{1,2}(-(alpha|beta|rc)[0-9]{2}){0,1}\.md$"

    # Search for matching files using the find command
    file=$(ls -1 "$directory" | grep -E "$version_name_regex")
    echo "$directory/$file"
}

fetch_updated_release_notes_file $1
