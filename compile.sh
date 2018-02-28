#!/bin/bash
PROGLIB=./lib/*.jar
javac -cp $PROGLIB ./nexusmachina/readfiletext/*.java
javac -cp $PROGLIB ./nexusmachina/calculaterelations/*.java
javac -cp $PROGLIB ./nexusmachina/storedata/*.java
