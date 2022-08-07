#!/bin/sh

echo "Use the VSCode Run and Debug panel. That should allow you to set breakpoints"
sleep 10

java \
    -cp $(find ../.gradle/caches -name 'Saxon-HE-9.9.1-7.jar'):./bin/main \
    com.philschatz.xslt.sourcemap.Main \
    -config:./serializer-config.xml \
    -s:./test/input.xml \
    -xsl:./test/transform.xsl \
    -o:./test/output.xml
