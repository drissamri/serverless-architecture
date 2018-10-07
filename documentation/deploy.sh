#!/usr/bin/env bash
gradle clean build
sls deploy function -f create-label -l