package com.philschatz.xslt.sourcemap;

import java.util.Stack;

import net.sf.saxon.expr.parser.Location;
import net.sf.saxon.om.Item;

public class SingletonHack {
    public static Stack<Location> currentInstructionContexts = new Stack<Location>();
    public static Stack<Item<?>> currentItems = new Stack<Item<?>>();
}
