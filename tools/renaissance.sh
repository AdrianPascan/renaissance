#!/bin/bash

BASE_DIR="$(dirname "$0")"
ROOT_DIR="$(git -C "$BASE_DIR" rev-parse --show-toplevel)"
RENAISSANCE_GIT_VERSION=$(git -C "$BASE_DIR" describe --dirty=-SNAPSHOT)
RENAISSANCE_VERSION=${RENAISSANCE_GIT_VERSION#v}

exec java -jar "$ROOT_DIR/target/renaissance-gpl-$RENAISSANCE_VERSION.jar" "$@"
