#!/bin/bash

libs_file="${GITHUB_WORKSPACE}/gradle/libs.versions.toml"
version_name_key="versionName"
version_name_regex="^[0-9]{1,2}\.[0-9]{1,2}\.[0-9]{1,2}(-(alpha|beta|rc)[0-9]{2}){0,1}$"

version=$(sed -n "s/.*${version_name_key}[[:space:]]*=[[:space:]]*[\"\']\(.*\)[\"\'].*/\1/p" ${libs_file})

if [[ ! ${version} =~ ${version_name_regex} ]]; then
    echo "Error: invalid version name [$version], please validate that [$version_name_key] at [$libs_file] follows regex $version_name_regex ."
    exit 1
fi

echo "$version"

exit 0
