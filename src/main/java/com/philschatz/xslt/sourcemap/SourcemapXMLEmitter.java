package com.philschatz.xslt.sourcemap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Base64;

import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;

import net.sf.saxon.expr.parser.Location;
import net.sf.saxon.om.NamespaceBindingSet;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.om.NodeName;
import net.sf.saxon.serialize.XMLEmitter;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.type.SchemaType;
import net.sf.saxon.type.SimpleType;

public class SourcemapXMLEmitter extends XMLEmitter {

    private SourceMapGenerator sourceMap = SourceMapGeneratorFactory.getInstance(SourceMapFormat.DEFAULT);

    private FilePosition lastWrite = new FilePosition(0, 0);

    private Location getNode() {
        if (Hack.outOfBandStack.size() > 0) {
            return Hack.outOfBandStack.peek();
        } else {
            return null;
        }
    }

    private FilePosition getCurrentPosition() {
        CountingWriter myWriter = (CountingWriter) writer;
        if (myWriter == null) {
            return new FilePosition(0, 0); // write may not be initialized yet
        }
        return new FilePosition(myWriter.cursorLine, myWriter.cursorColumn);
    }

    private String toRelative(Location source) {
        try {
            URI src = new URI(source.getSystemId());
            URI out = new URI(this.systemId);
            if ("file".equals(src.getScheme()) && "file".equals(out.getScheme())) {
                Path s = Path.of(src);
                Path o = Path.of(out);
                return o.getParent().relativize(s).toString();
            }
        } catch (URISyntaxException e) {
        }
        return source.toString();
    }

    private void addMapping(Location source, FilePosition start) {
        CountingWriter myWriter = (CountingWriter) writer;
        if (myWriter == null) {
            System.out.println("Skipping addMapping because writer has not been initialized yet");
        } else if (source == null) {
            System.out.println("Skipping addMapping because the source node is null");
        } else {
            Hack.printStack();
            FilePosition currentCursor = getCurrentPosition();
            System.out.println(String.format("addMapping from %d:%d-%d:%d to %s %d:%d", lastWrite.getLine(),
                    lastWrite.getColumn(), currentCursor.getLine(), currentCursor.getColumn(),
                    toRelative(source), source.getLineNumber() - 1, source.getColumnNumber() - 1));
            sourceMap.addMapping(toRelative(source), null,
                    new FilePosition(source.getLineNumber() - 1, source.getColumnNumber() - 1), lastWrite,
                    getCurrentPosition());

            lastWrite = currentCursor;
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

    @Override
    protected void writeDocType(NodeName name, String displayName, String systemId, String publicId)
            throws XPathException {
        super.writeDocType(name, displayName, systemId, publicId);
    }

    @Override
    public void close() throws XPathException {
        // Serialize the sourcemap and embed it to the bottom of the file
        // https://github.com/thlorenz/convert-source-map/issues/59
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        try {
            sourceMap.appendTo(ps, null);
        } catch (IOException e) {
            // Ignore Error
        }
        byte[] serialized = baos.toByteArray();
        String encoded = new String(Base64.getEncoder().encode(serialized));
        super.comment(new String(serialized), null, -1);
        super.comment(String.format("# sourceMappingURL=data:application/json;base64,%s", encoded), null, -1);
        super.close();
    }

    @Override
    public void startElement(NodeName elemName, SchemaType typeCode, Location location, int properties)
            throws XPathException {

        Location node = getNode();
        FilePosition start = getCurrentPosition();
        System.out.println(String.format("Serializer.startElement %s @ %s %d:%d", elemName.getDisplayName(),
                node.getSystemId(), node.getLineNumber(), node.getColumnNumber()));
        if (node instanceof NodeInfo) {
            NodeInfo n = (NodeInfo) node;
            if (n.getDisplayName() != elemName.getDisplayName()) {
                System.out.println("BUG: the source node above seems to be something else");
            }
        }
        super.startElement(elemName, typeCode, location, properties);
        addMapping(node, start);
    }

    @Override
    public void namespace(NamespaceBindingSet namespaceBindings, int properties) throws XPathException {
        FilePosition start = getCurrentPosition();
        super.namespace(namespaceBindings, properties);
        addMapping(getNode(), start);
    }

    @Override
    public void attribute(NodeName nameCode, SimpleType typeCode, CharSequence value, Location locationId,
            int properties)
            throws XPathException {
        FilePosition start = getCurrentPosition();
        Location node = getNode();
        System.out.println(String.format("Serializer.attribute %s @ %s %d:%d", nameCode.getDisplayName(),
                node.getSystemId(), node.getLineNumber(), node.getColumnNumber()));
        super.attribute(nameCode, typeCode, value, locationId, properties);
        addMapping(getNode(), start);
    }

    @Override
    public void closeStartTag() throws XPathException {
        System.out.println("Serializer.closeStartTag");
        FilePosition start = getCurrentPosition();
        super.closeStartTag();
        addMapping(getNode(), start);
    }

    // @Override
    // protected String emptyElementTagCloser(String displayName, NodeName nameCode)
    // {
    // return super.emptyElementTagCloser(displayName, nameCode);
    // }

    // @Override
    // protected void writeAttribute(NodeName elCode, String attname, CharSequence
    // value, int properties) throws XPathException {
    // super.writeAttribute(elCode, attname, value, properties);
    // }

    @Override
    public void endElement() throws XPathException {
        System.out.println("Serializer.endElement");
        FilePosition start = getCurrentPosition();
        super.endElement();
        addMapping(getNode(), start);
    }

    @Override
    public void characters(CharSequence chars, Location locationId, int properties) throws XPathException {
        System.out.println("Serializer.characters");
        FilePosition start = getCurrentPosition();
        super.characters(chars, locationId, properties);
        addMapping(getNode(), start);
    }

    @Override
    public void writeCharSequence(CharSequence s) throws java.io.IOException {
        System.out.println("Serializer.charSequence");
        FilePosition start = getCurrentPosition();
        super.writeCharSequence(s);
        addMapping(getNode(), start);
    }

    @Override
    public void processingInstruction(String target, CharSequence data, Location locationId, int properties)
            throws XPathException {
        System.out.println("Serializer.processingInstruction");
        FilePosition start = getCurrentPosition();
        super.processingInstruction(target, data, locationId, properties);
        addMapping(getNode(), start);
    }

    @Override
    public void comment(CharSequence chars, Location locationId, int properties) throws XPathException {
        System.out.println("Serializer.comment");
        FilePosition start = getCurrentPosition();
        super.comment(chars, locationId, properties);
        addMapping(getNode(), start);
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