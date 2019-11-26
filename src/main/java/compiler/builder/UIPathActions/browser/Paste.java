package compiler.builder.UIPathActions.browser;

import compiler.builder.UIPathActions.InvokeCode;
import compiler.builder.UIPathElement;
import compiler.builder.Variables;
import compiler.entity.Action;
import org.w3c.dom.Element;

public class Paste extends UIPathElement {
    private Element typeInto;
    private Action action;
    private InvokeCode invokeCode;

    public Paste(Element doSequence, Action action) {
        this.action = action;
        invokeCode = new InvokeCode(doSequence, action.getTargetName(), Variables.CLIPBOARD);
        createPaste(doSequence);
    }

    private void createPaste(Element doSequence) {
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
        String text = invokeCode.isElementPresent ? Variables.TRANSFORMED_VALUE : Variables.CLIPBOARD;
        typeInto.setAttribute("Text", "[" + text + "]");
    }

    @Override
    public Element getDomElement() {
        return typeInto;
    }
}
