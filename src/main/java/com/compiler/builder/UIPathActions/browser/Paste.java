package com.compiler.builder.UIPathActions.browser;

import com.compiler.builder.UIPathActions.InvokeCode;
import com.compiler.builder.UIPathElement;
import com.compiler.builder.Variables;
import com.compiler.entity.Action;
import org.w3c.dom.Element;

public class Paste extends UIPathElement {
    private Element typeInto;
    private Action action;
    private InvokeCode invokeCode;

    public Paste(Action action) {
        this.action = action;
        String input = action.isInDeclarativeMode() ? Variables.CELLS : Variables.CLIPBOARD;
        invokeCode = new InvokeCode(action.getTargetName(), input, action.isInDeclarativeMode());
        createPaste();
    }

    private void createPaste() {
        typeInto = doc.createElement("ui:TypeInto");
        setPasteAttributes();
        createTarget();
        doSequence.appendChild(typeInto);
    }

    private void createTarget() {
        Element typeIntoTarget = doc.createElement("ui:TypeInto.Target");
        Element target = doc.createElement("ui:Target");
        target.setAttribute("Selector", "&lt;webctrl name='" + action.getTargetName() + "' tag='INPUT' /&gt;");
        target.setAttribute("WaitForReady", "COMPLETE");

        typeIntoTarget.appendChild(target);
        typeInto.appendChild(typeIntoTarget);
    }

    private void setPasteAttributes() {
        typeInto.setAttribute("Activate", "True");
        typeInto.setAttribute("ClickBeforeTyping", "False");
        typeInto.setAttribute("DisplayName", "Type Into 'INPUT'");
        typeInto.setAttribute("EmptyField", "False");
        typeInto.setAttribute("SendWindowMessages", "False");
        typeInto.setAttribute("SimulateType", "True");
        typeInto.setAttribute("DelayBetweenKeys", "0");
        typeInto.setAttribute("DelayBefore", "0");
        typeInto.setAttribute("DelayMS", "0");
        String text = invokeCode.isElementPresent ? Variables.TRANSFORMED_VALUE : Variables.CLIPBOARD;
        typeInto.setAttribute("Text", "[" + text + "]");
    }

    @Override
    public Element getDomElement() {
        return typeInto;
    }
}
