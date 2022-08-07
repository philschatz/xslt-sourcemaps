package com.philschatz.xslt.sourcemap;

import net.sf.saxon.serialize.XMLEmitter;
import net.sf.saxon.trans.XPathException;

import java.io.IOException;
import java.io.Writer;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

import net.sf.saxon.expr.parser.Location;
import net.sf.saxon.om.Item;
import net.sf.saxon.om.NamespaceBindingSet;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.om.NodeName;
import net.sf.saxon.type.SchemaType;
import net.sf.saxon.type.SimpleType;

public class SourcemapXMLEmitter extends XMLEmitter {

    // public SourcemapXMLEmitter() {
    //     System.out.println("woot, constructed!");
    // }

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

    private Location getNode() {
        if (SingletonHack.currentInstructionContexts.size() > 0) {
            Location node = SingletonHack.currentInstructionContexts.peek();
            Item<?> item = SingletonHack.currentItems.peek();
            if (node != item) {
                System.out.println("OOOOOHHHHHH. Node and Item do not match. interesting");
            }
            return node;
        } else {
            return null;
        }
    }

    private void addMapping(Location source) {
        CountingWriter myWriter = (CountingWriter) writer;
        if (myWriter == null) {
            System.out.println("Skipping addMapping because writer has not been initialized yet");
        } else if (source == null) {
            System.out.println("Skipping addMapping because the source node is null");
        } else {
            System.out.println(String.format("addMapping from %d:%d to %s %d:%d", myWriter.cursorLine + 1, myWriter.cursorColumn + 1, source.getSystemId(), source.getLineNumber(), source.getColumnNumber()));
        }
    }

    @Override
    protected void makeWriter() throws XPathException {
        super.makeWriter();
        writer = new CountingWriter(writer);
    }

    @Override
    public void writeDeclaration() throws XPathException {
        super.writeDeclaration();
    }

    // @Override
    // protected void writeDocType(NodeName name, String displayName, String systemId, String publicId) throws XPathException {
    //     super.writeDocType(name, displayName, systemId, publicId);
    // }

    @Override
    public void close() throws XPathException {
        super.close();
        // Probably write the sourcemap file out now
    }

    @Override
    public void startElement(NodeName elemName, SchemaType typeCode, Location location, int properties) throws XPathException {
        Location node = getNode();
        System.out.println(String.format("Serializer.startElement %s @ %s %d:%d", elemName.getDisplayName(), node.getSystemId(), node.getLineNumber(), node.getColumnNumber()));
        addMapping(node);
        if (node instanceof NodeInfo) {
            NodeInfo n = (NodeInfo) node;
            if (n.getDisplayName() != elemName.getDisplayName()) {
                System.out.println("BUG: the source node above seems to be something else");
            }
        }
        super.startElement(elemName, typeCode, location, properties);
    }

    @Override
    public void namespace(NamespaceBindingSet namespaceBindings, int properties) throws XPathException {
        addMapping(getNode());
        super.namespace(namespaceBindings, properties);
    }

    @Override
    public void attribute(NodeName nameCode, SimpleType typeCode, CharSequence value, Location locationId, int properties)
            throws XPathException {
        Location node = getNode();
        System.out.println(String.format("Serializer.attribute %s @ %s %d:%d", nameCode.getDisplayName(), node.getSystemId(), node.getLineNumber(), node.getColumnNumber()));
        addMapping(getNode());
        super.attribute(nameCode, typeCode, value, locationId, properties);
    }

    @Override
    public void closeStartTag() throws XPathException {
        System.out.println("Serializer.closeStartTag");
        addMapping(getNode());
        super.closeStartTag();
    }

    // @Override
    // protected String emptyElementTagCloser(String displayName, NodeName nameCode) {
    //     return super.emptyElementTagCloser(displayName, nameCode);
    // }

    // @Override
    // protected void writeAttribute(NodeName elCode, String attname, CharSequence value, int properties) throws XPathException {
    //     super.writeAttribute(elCode, attname, value, properties);
    // }

    @Override
    public void endElement() throws XPathException {
        System.out.println("Serializer.endElement");
        addMapping(getNode());
        super.endElement();
    }

    @Override
    public void characters(CharSequence chars, Location locationId, int properties) throws XPathException {
        System.out.println("Serializer.characters");
        addMapping(getNode());
        super.characters(chars, locationId, properties);
    }

    @Override
    public void writeCharSequence(CharSequence s) throws java.io.IOException {
        System.out.println("Serializer.charSequence");
        addMapping(getNode());
        super.writeCharSequence(s);
    }

    @Override
    public void processingInstruction(String target, CharSequence data, Location locationId, int properties)
            throws XPathException {
        System.out.println("Serializer.processingInstruction");
        addMapping(getNode());
        super.processingInstruction(target, data, locationId, properties);
    }

    // @Override
    // protected void writeEscape(final CharSequence chars, final boolean inAttribute)
    //         throws java.io.IOException, XPathException {
    //     super.writeEscape(chars, inAttribute);
    // }

    @Override
    public void comment(CharSequence chars, Location locationId, int properties) throws XPathException {
        System.out.println("Serializer.comment");
        addMapping(getNode());
        super.comment(chars, locationId, properties);
    }
}

class CountingWriter extends Writer {
    private Writer underlying;
    public int cursorLine = 0;
    public int cursorColumn = 0;
    
    public CountingWriter(Writer underlying) {
        this.underlying = underlying;
    }
    @Override
    public void close() throws IOException {
        underlying.close();
    }
    @Override
    public void flush() throws IOException {
        underlying.flush();
    }
    public @Override void write(int c) throws IOException {
        System.out.println(String.format("Writing: '%c'", c));
        cursorColumn++;
        underlying.write(c);
    }
    public @Override void write(String str) throws IOException {
        System.out.println(String.format("Writing: '%s'", str));
        StringCharacterIterator it = new StringCharacterIterator(str);
        while (it.current() != CharacterIterator.DONE) {
            if (it.current() == '\n') {
                cursorColumn = 0;
                cursorLine++;
            } else {
                cursorColumn++;
            }
            it.next();
        }
        underlying.write(str);
    }
    
    @Override
    public void write(char[] buf, int start, int length) throws IOException {
        System.out.println(String.format("Writing: char[] '%s'", new String(buf, start, length)));
        for (int i = start; i < length; i++) {
            if (buf[i] == '\n') {
                cursorColumn = 0;
                cursorLine++;
            } else {
                cursorColumn++;
            }
        }
        underlying.write(buf, start, length);
    }

}