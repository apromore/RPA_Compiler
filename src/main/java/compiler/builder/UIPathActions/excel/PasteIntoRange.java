package compiler.builder.UIPathActions.excel;

import compiler.builder.UIPathElement;
import compiler.entity.Action;
import org.w3c.dom.Element;

public class PasteIntoRange extends UIPathElement implements ExcelRange {
    private Element writeRange;
    private Action action;

    public PasteIntoRange(Element doSequence, Action action) {
        this.action = action;
        createPasteIntoRange(doSequence);
    }

    private void createPasteIntoRange(Element doSequence) {
        writeRange = doc.createElement("ui:ExcelWriteRange");
        setAttributes(writeRange, action);
        doSequence.appendChild(writeRange);
    }

    @Override
    public Element getDomElement() {
        return writeRange;
    }
}
