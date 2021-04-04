#!/bin/bash
set -e

cd ..
SEMVER=`cat version.sbt | grep "version :=" | sed  -n 's/[^0-9]*\([0-9\.]\+\)[^0-9].*/\1/p'`
FULVER=`cat version.sbt | grep "version :=" | sed  -n 's/.*\"\([0-9A-Za-z\.\-]\+\)\".*/\1/p'`
echo "SEMVER: ${SEMVER}, FULVER: ${FULVER}"


# Gitlab pipeline
if [ "$CI_COMMIT_SHORT_SHA" ]
then
    VERSION="${SEMVER}-dev.$GITHUB_SHA"
fi

# Else (local)
if [ ! "$VERSION" ]
then
    VERSION="${SEMVER}-dev.`date +%s`"
fi

echo "Compilation: $VERSION"

# Update "version.sbt" file
sed -i "s/${FULVER}/${VERSION}/" version.sbt
