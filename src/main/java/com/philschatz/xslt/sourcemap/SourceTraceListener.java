package com.philschatz.xslt.sourcemap;

import net.sf.saxon.Controller;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.expr.instruct.TraceExpression;
import net.sf.saxon.expr.parser.Location;
import net.sf.saxon.lib.Logger;
import net.sf.saxon.lib.TraceListener;
import net.sf.saxon.om.Item;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.om.StandardNames;
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
        switch (instruction.getConstructType()) {
            case StandardNames.XSL_ELEMENT:
            case StandardNames.XSL_ATTRIBUTE:
            case StandardNames.XSL_COMMENT:
            case StandardNames.XSL_TEXT:
                Location instr = (Location) instruction;
                System.out.println(String.format("TraceListener:Pushing XSLT %s %d:%d", instr.getSystemId(), instr.getLineNumber(), instr.getColumnNumber()));
                SingletonHack.currentInstructionContexts.add(instr);
                break;
            case StandardNames.XSL_COPY:
            case StandardNames.XSL_COPY_OF:
                NodeInfo item = (NodeInfo) context.getContextItem();
                System.out.println(String.format("TraceListener:Pushing source %s %s %d:%d", item.getDisplayName(), item.getSystemId(), item.getLineNumber(), item.getColumnNumber()));
                SingletonHack.currentInstructionContexts.add(item);
                break;
            default:
                if (instruction.getConstructType() >= 1024) {
                    // This instruction is not a built-in one. So it _must_ be an instruction that creates a new element (e.g. `<para>` in the XSLT)
                    TraceExpression exp = (TraceExpression) instruction;
                    System.out.println(String.format("TraceListener:Pushing XSLT %s %d:%d", exp.getSystemId(), exp.getLineNumber(), exp.getColumnNumber()));
                    SingletonHack.currentInstructionContexts.add(exp);
                } else {
                    // Example: <xsl:template>
                    System.out.println(String.format("TraceListener:Pushing Something else!!!!"));
                    SingletonHack.currentInstructionContexts.add(null);
                }
        }
    }

    @Override
    public void leave(InstructionInfo instruction) {
        System.out.println(String.format("TraceListener:Popping (%d left)", SingletonHack.currentInstructionContexts.size()));
        SingletonHack.currentInstructionContexts.pop();
    }

    @SuppressWarnings("all")
    @Override
    public void startCurrentItem(Item currentItem) {
        SingletonHack.currentItems.push(currentItem);
    }

    @SuppressWarnings("all")
    @Override
    public void endCurrentItem(Item currentItem) {
        // SingletonHack.currentItems.pop();
        if (!SingletonHack.currentItems.pop().equals(currentItem)) {
            throw new Error("BUG: THe number of items on the stack are wrong");
        }
    }
    
}