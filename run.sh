#!/bin/bash
java -Xmx4096M -classpath ".:./lib/*:./modules/readfiletext/*" nexusmachina.readfiletext.Main ./data/inputfiles/pg345.txt
java -Xmx4096M -classpath ".:./lib/*:./modules/calculaterelations/*" nexusmachina.calculaterelations.Main ./io/output-readfiletext.json
java -Xmx4096M -classpath ".:./lib/*:./modules/storedata/*" nexusmachina.storedata.Main ./io/output-calculaterelations.json
