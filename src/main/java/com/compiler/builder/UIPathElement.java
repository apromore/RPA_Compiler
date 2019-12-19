package com.compiler.builder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class UIPathElement {
    public static Document doc;
    public static Element doSequence;

    public UIPathElement appendChild(UIPathElement element) {
        getDomElement().appendChild(element.getDomElement());
        return element;
    }

    public abstract Element getDomElement();

    public static Element getDoSequence() {
        return doSequence;
    }

    public static void setDoSequence(Element doSequence) {
        UIPathElement.doSequence = doSequence;
    }
}
