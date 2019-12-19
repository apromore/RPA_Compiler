package com.compiler.builder;

import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

public class Imports extends UIPathElement {
    private Element namespace;

    public Imports() {
        namespace = doc.createElement("TextExpression.NamespacesForImplementation");
        createImportCollection();
    }

    private void createImportCollection() {
        Element collection = doc.createElement("sco:Collection");
        collection.setAttribute("x:TypeArguments", "x:String");
        createImports(collection);
        namespace.appendChild(collection);
    }

    private void createImports(Element collection) {
        List<Element> imports = new ArrayList<>();

        Element uiPathCore = doc.createElement("x:String");
        uiPathCore.setTextContent("UiPath.Core");
        imports.add(uiPathCore);
        Element activities = doc.createElement("x:String");
        activities.setTextContent("UiPath.Core.Activities");
        imports.add(activities);
        Element system = doc.createElement("x:String");
        system.setTextContent("System");
        imports.add(system);
        Element generic = doc.createElement("x:String");
        generic.setTextContent("System.Collections.Generic");
        imports.add(generic);
        Element regularExpressions = doc.createElement("x:String");
        regularExpressions.setTextContent("System.Text.RegularExpressions");
        imports.add(regularExpressions);
        Element linq = doc.createElement("x:String");
        linq.setTextContent("System.Linq");
        imports.add(linq);

        imports.forEach(collection::appendChild);
    }

    @Override
    public Element getDomElement() {
        return namespace;
    }
}
