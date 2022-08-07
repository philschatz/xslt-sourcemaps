package com.philschatz.xslt.sourcemap;

import java.util.Properties;
import net.sf.saxon.Configuration;
import net.sf.saxon.lib.SerializerFactory;
import net.sf.saxon.serialize.Emitter;

public class Factory extends SerializerFactory {

    public Factory() {
        super(new Configuration());
    }

    public Emitter newXMLEmitter(Properties properties) {
        return new SourcemapXMLEmitter();
    }
}
