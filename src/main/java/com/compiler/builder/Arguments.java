package com.compiler.builder;

import org.w3c.dom.Element;

public class Arguments extends UIPathElement {
    public static final String ROW = "row";
    public static final String COLUMN = "column";
    private Element members;

    public Arguments() {
        members = doc.createElement("x:Members");
        createPropertyRow();
        createPropertyColumn();
    }

    private void createPropertyRow() {
        Element property = doc.createElement("x:Property");
        property.setAttribute("Name", ROW);
        property.setAttribute("Type", "InArgument(x:String)");
        members.appendChild(property);
    }

    private void createPropertyColumn() {
        Element property = doc.createElement("x:Property");
        property.setAttribute("Name", COLUMN);
        property.setAttribute("Type", "InArgument(x:String)");
        members.appendChild(property);
    }

    @Override
    public Element getDomElement() {
        return members;
    }
}
