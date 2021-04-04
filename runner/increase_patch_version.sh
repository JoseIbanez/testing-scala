#!/bin/bash
set -e

cd ..
cat version.sbt
echo

# Dealing different behaviour of sed command in Mac vs Linux
if [ "$(uname)" == "Darwin" ]; then
    SEDCMD=gsed        
elif [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
    SEDCMD=sed
else
    echo "OS not supported"
    exit 1
fi

# Get current verion
SEMVER=`cat version.sbt | grep "version :=" | $SEDCMD  -n 's/[^0-9]*\([0-9\.]\+\)[^0-9].*/\1/p'`
FULVER=`cat version.sbt | grep "version :=" | $SEDCMD  -n 's/.*\"\([0-9A-Za-z\.\-]\+\)\".*/\1/p'`
echo "OLD: SEMVER: ${SEMVER}, FULVER: ${FULVER}"

# Read Semver fields
IFS=. read -r major minor patch <<< "$SEMVER"
((patch++))

# New SEMVER 
NEWVER="$major.$minor.$patch-SNAPSHOT"
echo "NEW: SEMVER: ${NEWVER}"

# Update "version.sbt" file
$SEDCMD -i "s/${FULVER}/${NEWVER}/" version.sbt
