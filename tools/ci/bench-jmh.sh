#!/bin/bash
source "$(dirname "$0")/common.sh"

# Test benchmarks under JMH harness using the 'test' configuration
# and replacing incompatible benchmarks with 'dummy-empty'.

LC_ALL=C.UTF-8 java -jar "$RENAISSANCE_JMH_JAR" \
	-jvmArgs -Xms2500M -jvmArgs -Xmx2500m \
	-jvmArgs -Dorg.renaissance.jmh.configuration=test \
	-jvmArgs -Dorg.renaissance.jmh.fakeIncompatible=true \
	-wi 0 -i 1 -f 1 -foe true
