package com.compiler.builder.UIPathActions.browser;

import com.compiler.builder.UIPathElement;
import com.compiler.entity.Action;
import org.w3c.dom.Element;

public class ClickCheckbox extends UIPathElement {
    private Element check;
    private Action action;

    public ClickCheckbox(Element doSequence, Action action) {
        this.action = action;
        createCheck(doSequence);
    }

    private void createCheck(Element doSequence) {
        check = doc.createElement("ui:Check");
        setAttributes();
        createTarget();
        doSequence.appendChild(check);
    }

    private void setAttributes() {
        check.setAttribute("compilator.com.com.compiler.compiler.entity.Action", "Check");
        check.setAttribute("DisplayName", "Check");
    }

    private void createTarget() {
        Element checkTarget = doc.createElement("ui:Check.Target");
        Element target = doc.createElement("ui:Target");
        target.setAttribute("Selector", "&lt;webctrl id='" + action.getTargetId() + "' tag='INPUT' /&gt;");
        target.setAttribute("WaitForReady", "COMPLETE");

        checkTarget.appendChild(checkTarget).appendChild(target);
    }

    @Override
    public Element getDomElement() {
        return check;
    }
}
