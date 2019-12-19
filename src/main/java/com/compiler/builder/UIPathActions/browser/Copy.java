package com.compiler.builder.UIPathActions.browser;

import com.compiler.builder.UIPathElement;
import com.compiler.builder.Variables;
import com.compiler.entity.Action;
import org.w3c.dom.Element;

public class Copy extends UIPathElement {
    private Element getValue;
    private Action action;

    public Copy(Action action) {
        this.action = action;
        createCopy();
    }

    private void createCopy() {
        getValue = doc.createElement("ui:GetValue");
        getValue.setAttribute("DisplayName", "Get Text 'INPUT'");
        createTarget();
        doSequence.appendChild(getValue);
    }

    private void createTarget() {
        Element getValueTarget = doc.createElement("ui:GetValue.Target");
        Element target = doc.createElement("ui:Target");
        target.setAttribute("Selector", "&lt;webctrl name='" + action.getTargetName() + "' tag='INPUT' /&gt;");
        target.setAttribute("WaitForReady", "INTERACTIVE");

        Element getValueValue = doc.createElement("ui:GetValue.Value");
        Element outArgument = doc.createElement("OutArgument");
        outArgument.setAttribute("x:TypeArguments", "x:String");
        outArgument.setTextContent("[" + Variables.CLIPBOARD + "]");

        getValue.appendChild(getValueValue).appendChild(outArgument);
        getValue.appendChild(getValueTarget).appendChild(target);
    }

    @Override
    public Element getDomElement() {
        return getValue;
    }
}
