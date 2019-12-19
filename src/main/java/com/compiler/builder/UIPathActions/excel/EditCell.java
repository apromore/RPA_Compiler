package com.compiler.builder.UIPathActions.excel;

import com.compiler.builder.UIPathElement;
import com.compiler.entity.Action;
import org.w3c.dom.Element;

public class EditCell extends UIPathElement {
    private Element excelWriteCell;
    private Action action;

    public EditCell(Element doSequence, Action action) {
        this.action = action;
        excelWriteCell = doc.createElement("ui:ExcelWriteCell");
        setAttributes();
        doSequence.appendChild(excelWriteCell);
    }

    private void setAttributes() {
        String cell = action.getTargetId();
        String sheetName = action.getSheetName();
        String text = action.getTargetValue();
        excelWriteCell.setAttribute("Cell", cell);
        excelWriteCell.setAttribute("DisplayName", "Write Cell");
        excelWriteCell.setAttribute("SheetName", sheetName);
        excelWriteCell.setAttribute("Text", text);
    }

    @Override
    public Element getDomElement() {
        return excelWriteCell;
    }
}
