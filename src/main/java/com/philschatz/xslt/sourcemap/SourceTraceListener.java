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
        NodeInfo item = (NodeInfo) context.getContextItem();
        System.out.println(String.format("TraceListener.enter %s %s", Hack.toDisplay(instruction), item.toShortString()));

        // switch (instruction.getConstructType()) {
        //     case StandardNames.XSL_ELEMENT:
        //     case StandardNames.XSL_ATTRIBUTE:
        //     case StandardNames.XSL_COMMENT:
        //     case StandardNames.XSL_TEXT:
        //         Location instr = (Location) instruction;
        //         Hack.outOfBandStack.add(instr);
        //         break;
        //     case StandardNames.XSL_COPY:
        //     case StandardNames.XSL_COPY_OF:
        //         Hack.outOfBandStack.add(item);
        //         break;
        //     default:
        //         if (instruction.getConstructType() >= 1024) {
        //             // This instruction is not a built-in one. So it _must_ be an instruction that creates a new element (e.g. `<para>` in the XSLT)
        //             Hack.outOfBandStack.add(instruction);
        //         }
        // }
    }

    @Override
    public void leave(InstructionInfo instruction) {
        System.out.println(String.format("TraceListener:leave %s (%d left)", Hack.toDisplay(instruction), Hack.outOfBandStack.size()));
        // switch (instruction.getConstructType()) {
        //     case StandardNames.XSL_ELEMENT:
        //     case StandardNames.XSL_ATTRIBUTE:
        //     case StandardNames.XSL_COMMENT:
        //     case StandardNames.XSL_TEXT:
        //     case StandardNames.XSL_COPY:
        //     case StandardNames.XSL_COPY_OF:
        //         Hack.outOfBandStack.pop();
        //         break;
        //     default: // Do nothing
        // }
    }

    @SuppressWarnings("all")
    @Override
    public void startCurrentItem(Item currentItem) {
        NodeInfo item = (NodeInfo) currentItem;
        System.out.println(String.format("TraceListener:startItem %s", item.toShortString()));
        Hack.outOfBandStack.add(item);
    }

    @SuppressWarnings("all")
    @Override
    public void endCurrentItem(Item currentItem) {
        NodeInfo item = (NodeInfo) currentItem;
        System.out.println(String.format("TraceListener:endItem  %s", item.toShortString()));
        Hack.outOfBandStack.pop();
    }
}