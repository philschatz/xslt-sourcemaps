package com.philschatz.xslt.sourcemap;

import net.sf.saxon.Controller;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.Logger;
import net.sf.saxon.lib.TraceListener;
import net.sf.saxon.om.Item;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.trace.InstructionInfo;

public class SourceTraceListener implements TraceListener {

    @Override
    public void setOutputDestination(Logger stream) {
    }

    @Override
    public void open(Controller controller) {
    }

    @Override
    public void close() {
    }

    @Override
    public void enter(InstructionInfo instruction, XPathContext context) {
        System.out.println(String.format("================ TraceListener.enter: %s:%d", instruction.getSystemId(), instruction.getLineNumber()));
        Item<?> item = context.getContextItem();
        if (item instanceof NodeInfo) {
            NodeInfo n = (NodeInfo) item;
            System.out.println(String.format("                             context: %s:%d %s", n.getSystemId(), n.getLineNumber(), n.getDisplayName()));
        } else {
            System.out.println(String.format("                             context: %s", context.getContextItem()));
        }
    }

    @Override
    public void leave(InstructionInfo instruction) {
        System.out.println(String.format("================ TraceListener.leave %s:%d", instruction.getSystemId(), instruction.getLineNumber()));
    }

    @SuppressWarnings("all")
    @Override
    public void startCurrentItem(Item currentItem) {
        System.out.println("================ TraceListener.startCurrentItem");
        System.out.println(currentItem.toShortString());
        System.out.println("================");
    }

    @SuppressWarnings("all")
    @Override
    public void endCurrentItem(Item currentItem) {
        System.out.println("================ TraceListener.endCurrentItem");
        System.out.println(currentItem.toShortString());
        System.out.println("================");
    }
    
}