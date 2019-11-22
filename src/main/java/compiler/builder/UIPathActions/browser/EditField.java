package compiler.builder.UIPathActions.browser;

import compiler.builder.UIPathElement;
import compiler.entity.Action;
import org.w3c.dom.Element;

public class EditField extends UIPathElement {
    private Element typeInto;
    private Action action;

    public EditField(Element doSequence, Action action) {
        this.action = action;
        createTypeInto(doSequence);
    }

    private void createTypeInto(Element doSequence) {
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
        typeInto.setAttribute("DisplayName", "Edit Field");
        typeInto.setAttribute("Text", action.getTargetValue());
        typeInto.setAttribute("Activate", "True");
        typeInto.setAttribute("ClickBeforeTyping", "False");
        typeInto.setAttribute("EmptyField", "False");
        typeInto.setAttribute("SendWindowMessages", "False");
        typeInto.setAttribute("SimulateType", "True");
    }

    @Override
    public org.w3c.dom.Element getDomElement() {
        return typeInto;
    }
}