package com.compiler.builder.UIPathActions.excel;

import com.compiler.builder.UIPathElement;
import com.compiler.entity.Action;
import org.w3c.dom.Element;

public class PasteIntoRange extends UIPathElement implements ExcelRange {
    private Element writeRange;
    private Action action;

    public PasteIntoRange(Action action) {
        this.action = action;
        createPasteIntoRange();
    }

    private void createPasteIntoRange() {
        writeRange = doc.createElement("ui:ExcelWriteRange");
        setAttributes(writeRange, action);
        doSequence.appendChild(writeRange);
    }

    @Override
    public Element getDomElement() {
        return writeRange;
    }
}
