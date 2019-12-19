package com.compiler.builder.UIPathActions.browser;

import com.compiler.builder.UIPathElement;
import com.compiler.entity.Action;
import org.w3c.dom.Element;

public class ClickLink extends UIPathElement {
    private Element click;
    private Action action;

    public ClickLink(Action action) {
        this.action = action;
        createClickLink();
    }

    private void createClickLink() {
        click = doc.createElement("ui:Click");
        setAttributes();
        createTarget();
        doSequence.appendChild(click);
    }

    private void createTarget() {
        String innerText = action.getTargetInnerText();
        String href = action.getTargetHref();
        String targetClass = action.getTargetClass();

        Element clickTarget = doc.createElement("ui:Click.Target");
        Element target = doc.createElement("ui:Target");
        target.setAttribute("Selector", "&lt;webctrl innerText='" + innerText +
                "' tag='A' href='" + href + "' class='" + targetClass + "'/&gt;");
        target.setAttribute("WaitForReady", "COMPLETE");

        click.appendChild(clickTarget).appendChild(target);
    }

    private void setAttributes() {
        click.setAttribute("ClickType", "CLICK_SINGLE");
        click.setAttribute("DisplayName", "Click " + action.getTargetHref() + "'");
        click.setAttribute("KeyModifiers", "None");
        click.setAttribute("MouseButton", "BTN_LEFT");
        click.setAttribute("SendWindowMessages", "False");
        click.setAttribute("SimulateClick", "True");
    }

    @Override
    public Element getDomElement() {
        return click;
    }
}
