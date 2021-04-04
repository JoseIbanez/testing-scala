#!/bin/bash
set -e
cd ..

# for scoverage 
#sbt clean coverage test
#sbt coverageReport
#sbt sonarScan

# for jacoco
sbt clean test
#sbt jacoco
#sbt sonarScan


ls -l ./target/test-reports/
ls -l ./target/scala-2.13/jacoco/
