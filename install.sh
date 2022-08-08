./gradlew clean
./gradlew dependencies

echo "If in VSCode or GitPod, then run CodeLens in the 'Run & Debug' Activity panel on the left side."
sleep 2

./gradlew build

java \
    -cp $(find ~/.gradle/ -name 'Saxon-HE-9.9.1-7.jar'):./bin/main \
    com.philschatz.xslt.sourcemap.Main \
    -config:./serializer-config.xml \
    -s:./test/input.xml \
    -xsl:./test/transform.xsl \
    -o:./test/output.xml
