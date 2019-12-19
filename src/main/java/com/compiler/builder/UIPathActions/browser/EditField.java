package com.compiler.builder.UIPathActions.browser;

import com.compiler.builder.UIPathActions.InvokeCode;
import com.compiler.builder.UIPathElement;
import com.compiler.builder.Variables;
import com.compiler.entity.Action;
import org.w3c.dom.Element;

public class EditField extends UIPathElement {
    private Element typeInto;
    private Action action;
    private InvokeCode invokeCode;

    public EditField(Action action) {
        this.action = action;
        invokeCode = new InvokeCode(action.getTargetName(), "\"" + action.getTargetValue() + "\"", false);
        createTypeInto();
    }

    private void createTypeInto() {
        typeInto = doc.createElement("ui:TypeInto");
        setTypeIntoAttributes();
        createTarget();
        doSequence.appendChild(typeInto);
    }

    private void createTarget() {
        Element typeIntoTarget = doc.createElement("ui:TypeInto.Target");
        Element target = doc.createElement("ui:Target");
        target.setAttribute("Selector", '&' + "lt;webctrl name='" + action.getTargetName() + "' tag='INPUT' /&gt;");
        target.setAttribute("WaitForReady", "COMPLETE");

        typeInto.appendChild(typeIntoTarget).appendChild(target);
    }

    private void setTypeIntoAttributes() {
        typeInto.setAttribute("DisplayName", "Edit Field " + action.getTargetName());
        String transformedValue = "[" + Variables.TRANSFORMED_VALUE + "]";
        String actionValue = "\"" + action.getTargetValue() + "\"";
        String text = invokeCode.isElementPresent ? transformedValue : actionValue;
        typeInto.setAttribute("Text", text);
        typeInto.setAttribute("Activate", "True");
        typeInto.setAttribute("ClickBeforeTyping", "False");
        typeInto.setAttribute("EmptyField", "False");
        typeInto.setAttribute("SendWindowMessages", "False");
        typeInto.setAttribute("SimulateType", "True");
        typeInto.setAttribute("DelayBetweenKeys", "0");
        typeInto.setAttribute("DelayBefore", "0");
        typeInto.setAttribute("DelayMS", "0");
    }

    @Override
    public org.w3c.dom.Element getDomElement() {
        return typeInto;
    }
}
