package com.philschatz.xslt.sourcemap;

import net.sf.saxon.serialize.XMLEmitter;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.expr.parser.Location;


import net.sf.saxon.om.NodeName;
import net.sf.saxon.type.SchemaType;

public class SourcemapXMLEmitter extends XMLEmitter {

    public SourcemapXMLEmitter() {
        System.out.println("woot, constructed!");
    }

    // @Override
    // @SuppressWarnings("all")
    // public void append(Item item, Location locationId, int copyNamespaces)  throws XPathException {
    //     throw new XPathException("append ASLKDJALSKJDASLKDWOQIEUQOWUEQWOIEU");
    //     // System.out.println("-----------START");
    //     // System.out.println(item);
    //     // System.out.println(locationId.getLineNumber());

    //     // super.append(item, locationId, copyNamespaces);
    //     // System.out.println("--------END");
    // }

    // public void open() throws XPathException {
    // protected void openDocument() throws XPathException {
    //     throw new XPathException("openDOcument ASOIDJASOIDJ");
    // }

    @Override
    public void startElement(NodeName elemName, SchemaType typeCode, Location location, int properties) throws XPathException {
        System.out.println("Serializer.startElement");
        System.out.println(elemName.getDisplayName());
    }

    @Override
    public void endElement() throws XPathException {
        System.out.println("Serializer.endElement");
    }
}