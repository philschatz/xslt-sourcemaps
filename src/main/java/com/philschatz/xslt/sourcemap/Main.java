package com.philschatz.xslt.sourcemap;

import net.sf.saxon.Transform;

public class Main {
    public static void main(String[] args) {
        new Transform().doTransform(args, "java net.sf.saxon.Transform");
    }
}