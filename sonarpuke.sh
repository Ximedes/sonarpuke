#!/usr/bin/env bash

mvn clean package -DskipTests

java -jar `find target -name "*dependencies.jar"` $@