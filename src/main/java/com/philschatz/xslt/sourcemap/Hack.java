package com.philschatz.xslt.sourcemap;

import java.util.Stack;

import net.sf.saxon.expr.parser.Location;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.om.StandardNames;
import net.sf.saxon.trace.InstructionInfo;

public class Hack {
    public static Stack<Location> outOfBandStack = new Stack<Location>();

    public static void printStack() {
        // Pretty-printer. Move it to a function
        System.out.print("SingletonHack. Here is the stack:");
        for (Location i : Hack.outOfBandStack) {
            System.out.print(String.format(" %s", toDisplay(i)));
        }
        System.out.println();
    }

    public static String toDisplay(Location loc) {
        if (loc instanceof NodeInfo) {
            return ((NodeInfo) loc).toShortString();
        } else if (loc instanceof InstructionInfo) {
            switch (((InstructionInfo) loc).getConstructType()) {
                case StandardNames.XSL_ACCEPT: return "XSL_ACCEPT";
                case StandardNames.XSL_ACCUMULATOR: return "XSL_ACCUMULATOR";
                case StandardNames.XSL_ACCUMULATOR_RULE: return "XSL_ACCUMULATOR_RULE";
                case StandardNames.XSL_ANALYZE_STRING: return "XSL_ANALYZE_STRING";
                case StandardNames.XSL_APPLY_IMPORTS: return "XSL_APPLY_IMPORTS";
                case StandardNames.XSL_APPLY_TEMPLATES: return "XSL_APPLY_TEMPLATES";
                case StandardNames.XSL_ASSERT: return "XSL_ASSERT";
                case StandardNames.XSL_ATTRIBUTE: return "XSL_ATTRIBUTE";
                case StandardNames.XSL_ATTRIBUTE_SET: return "XSL_ATTRIBUTE_SET";
                case StandardNames.XSL_BREAK: return "XSL_BREAK";
                case StandardNames.XSL_CALL_TEMPLATE: return "XSL_CALL_TEMPLATE";
                case StandardNames.XSL_CATCH: return "XSL_CATCH";
                case StandardNames.XSL_CHARACTER_MAP: return "XSL_CHARACTER_MAP";
                case StandardNames.XSL_CHOOSE: return "XSL_CHOOSE";
                case StandardNames.XSL_COMMENT: return "XSL_COMMENT";
                case StandardNames.XSL_CONTEXT_ITEM: return "XSL_CONTEXT_ITEM";
                case StandardNames.XSL_COPY: return "XSL_COPY";
                case StandardNames.XSL_COPY_OF: return "XSL_COPY_OF";
                case StandardNames.XSL_DECIMAL_FORMAT: return "XSL_DECIMAL_FORMAT";
                case StandardNames.XSL_DOCUMENT: return "XSL_DOCUMENT";
                case StandardNames.XSL_ELEMENT: return "XSL_ELEMENT";
                case StandardNames.XSL_EXPOSE: return "XSL_EXPOSE";
                case StandardNames.XSL_EVALUATE: return "XSL_EVALUATE";
                case StandardNames.XSL_FALLBACK: return "XSL_FALLBACK";
                case StandardNames.XSL_FOR_EACH: return "XSL_FOR_EACH";
                case StandardNames.XSL_FORK: return "XSL_FORK";
                case StandardNames.XSL_FOR_EACH_GROUP: return "XSL_FOR_EACH_GROUP";
                case StandardNames.XSL_FUNCTION: return "XSL_FUNCTION";
                case StandardNames.XSL_GLOBAL_CONTEXT_ITEM: return "XSL_GLOBAL_CONTEXT_ITEM";
                case StandardNames.XSL_IF: return "XSL_IF";
                case StandardNames.XSL_IMPORT: return "XSL_IMPORT";
                case StandardNames.XSL_IMPORT_SCHEMA: return "XSL_IMPORT_SCHEMA";
                case StandardNames.XSL_INCLUDE: return "XSL_INCLUDE";
                case StandardNames.XSL_ITERATE: return "XSL_ITERATE";
                case StandardNames.XSL_KEY: return "XSL_KEY";
                case StandardNames.XSL_MAP: return "XSL_MAP";
                case StandardNames.XSL_MAP_ENTRY: return "XSL_MAP_ENTRY";
                case StandardNames.XSL_MATCHING_SUBSTRING: return "XSL_MATCHING_SUBSTRING";
                case StandardNames.XSL_MERGE: return "XSL_MERGE";
                case StandardNames.XSL_MERGE_ACTION: return "XSL_MERGE_ACTION";
                case StandardNames.XSL_MERGE_KEY: return "XSL_MERGE_KEY";
                case StandardNames.XSL_MERGE_SOURCE: return "XSL_MERGE_SOURCE";
                case StandardNames.XSL_MESSAGE: return "XSL_MESSAGE";
                case StandardNames.XSL_MODE: return "XSL_MODE";
                case StandardNames.XSL_NAMESPACE: return "XSL_NAMESPACE";
                case StandardNames.XSL_NAMESPACE_ALIAS: return "XSL_NAMESPACE_ALIAS";
                case StandardNames.XSL_NEXT_ITERATION: return "XSL_NEXT_ITERATION";
                case StandardNames.XSL_NEXT_MATCH: return "XSL_NEXT_MATCH";
                case StandardNames.XSL_NON_MATCHING_SUBSTRING: return "XSL_NON_MATCHING_SUBSTRING";
                case StandardNames.XSL_NUMBER: return "XSL_NUMBER";
                case StandardNames.XSL_OTHERWISE: return "XSL_OTHERWISE";
                case StandardNames.XSL_ON_COMPLETION: return "XSL_ON_COMPLETION";
                case StandardNames.XSL_ON_EMPTY: return "XSL_ON_EMPTY";
                case StandardNames.XSL_ON_NON_EMPTY: return "XSL_ON_NON_EMPTY";
                case StandardNames.XSL_OUTPUT: return "XSL_OUTPUT";
                case StandardNames.XSL_OVERRIDE: return "XSL_OVERRIDE";
                case StandardNames.XSL_OUTPUT_CHARACTER: return "XSL_OUTPUT_CHARACTER";
                case StandardNames.XSL_PACKAGE: return "XSL_PACKAGE";
                case StandardNames.XSL_PARAM: return "XSL_PARAM";
                case StandardNames.XSL_PERFORM_SORT: return "XSL_PERFORM_SORT";
                case StandardNames.XSL_PRESERVE_SPACE: return "XSL_PRESERVE_SPACE";
                case StandardNames.XSL_PROCESSING_INSTRUCTION: return "XSL_PROCESSING_INSTRUCTION";
                case StandardNames.XSL_RESULT_DOCUMENT: return "XSL_RESULT_DOCUMENT";
                case StandardNames.XSL_SEQUENCE: return "XSL_SEQUENCE";
                case StandardNames.XSL_SORT: return "XSL_SORT";
                case StandardNames.XSL_SOURCE_DOCUMENT: return "XSL_SOURCE_DOCUMENT";
                case StandardNames.XSL_STRIP_SPACE: return "XSL_STRIP_SPACE";
                case StandardNames.XSL_STYLESHEET: return "XSL_STYLESHEET";
                case StandardNames.XSL_TEMPLATE: return "XSL_TEMPLATE";
                case StandardNames.XSL_TEXT: return "XSL_TEXT";
                case StandardNames.XSL_TRANSFORM: return "XSL_TRANSFORM";
                case StandardNames.XSL_TRY: return "XSL_TRY";
                case StandardNames.XSL_USE_PACKAGE: return "XSL_USE_PACKAGE";
                case StandardNames.XSL_VALUE_OF: return "XSL_VALUE_OF";
                case StandardNames.XSL_VARIABLE: return "XSL_VARIABLE";
                case StandardNames.XSL_WHEN: return "XSL_WHEN";
                case StandardNames.XSL_WITH_PARAM: return "XSL_WITH_PARAM";
                case StandardNames.XSL_WHERE_POPULATED: return "XSL_WHERE_POPULATED";
            
                case StandardNames.XSL_DEFAULT_COLLATION: return "XSL_DEFAULT_COLLATION";
                case StandardNames.XSL_DEFAULT_MODE: return "XSL_DEFAULT_MODE";
                case StandardNames.XSL_DEFAULT_VALIDATION: return "XSL_DEFAULT_VALIDATION";
                case StandardNames.XSL_EXCLUDE_RESULT_PREFIXES: return "XSL_EXCLUDE_RESULT_PREFIXES";
                case StandardNames.XSL_EXPAND_TEXT: return "XSL_EXPAND_TEXT";
                case StandardNames.XSL_EXTENSION_ELEMENT_PREFIXES: return "XSL_EXTENSION_ELEMENT_PREFIXES";
                case StandardNames.XSL_INHERIT_NAMESPACES: return "XSL_INHERIT_NAMESPACES";
                case StandardNames.XSL_TYPE: return "XSL_TYPE";
                case StandardNames.XSL_USE_ATTRIBUTE_SETS: return "XSL_USE_ATTRIBUTE_SETS";
                case StandardNames.XSL_USE_WHEN: return "XSL_USE_WHEN";
                case StandardNames.XSL_VALIDATION: return "XSL_VALIDATION";
                case StandardNames.XSL_VERSION: return "XSL_VERSION";
                case StandardNames.XSL_XPATH_DEFAULT_NAMESPACE: return "XSL_XPATH_DEFAULT_NAMESPACE";
    
                default:
                    if (((InstructionInfo) loc).getConstructType() >= 1024) {
                        return "xsl_element";
                    }
            }                
        }
        throw new Error("BUG: Unknown instruction type");
    }
}
