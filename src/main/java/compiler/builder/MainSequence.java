package compiler.builder;

import org.w3c.dom.Element;

public class MainSequence extends UIPathElement {
    private Element mainSequence;

    MainSequence() {
        mainSequence = doc.createElement("Sequence");
        mainSequence.setAttribute("DisplayName", "Main Sequence");
    }

    @Override
    public Element getDomElement() {
        return mainSequence;
    }
}
