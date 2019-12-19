package com.compiler.builder.UIPathActions.excel;

import com.compiler.builder.UIPathElement;
import com.compiler.builder.Variables;
import com.compiler.entity.Action;
import org.w3c.dom.Element;

public class PasteIntoCell extends UIPathElement {
    private Element writeCell;
    private Action action;

    public PasteIntoCell(Action action) {
        this.action = action;

        writeCell = doc.createElement("ui:ExcelWriteCell");
        setAttributes();
        doSequence.appendChild(writeCell);
    }

    private void setAttributes() {
        String cell = action.getTargetId();
        String sheetName = action.getSheetName();
        writeCell.setAttribute("DisplayName", "Paste Cell");
        writeCell.setAttribute("Cell", cell);
        writeCell.setAttribute("SheetName", sheetName);
        writeCell.setAttribute("Text", "[" + Variables.CLIPBOARD + "]");
    }

    @Override
    public Element getDomElement() {
        return writeCell;
    }
}
