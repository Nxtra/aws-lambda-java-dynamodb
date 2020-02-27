#!/bin/bash

set -e

./gradlew clean build
sls deploy -v