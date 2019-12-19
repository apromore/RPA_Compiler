package com.compiler.builder.UIPathActions.excel;

import com.compiler.builder.UIPathElement;
import com.compiler.entity.Action;
import org.w3c.dom.Element;

public class CopyRange extends UIPathElement {
    private Element readRange;
    private Action action;

    public CopyRange(Element doSequence, Action action) {
        this.action = action;
        readRange = doc.createElement("ui:ExcelReadRange");
        setAttributes();
        doSequence.appendChild(readRange);
    }

    private void setAttributes() {
        String firstCell = getFirstCell(action.getTargetId());
        String sheetName = action.getSheetName();
        readRange.setAttribute("AddHeaders", "False");
        readRange.setAttribute("DataTable", "[dataTable]");
        readRange.setAttribute("DisplayName", "Read Range");
        readRange.setAttribute("Range", firstCell);
        readRange.setAttribute("SheetName", sheetName);
    }

    private String getFirstCell(String range) {
        return range.substring(0, range.indexOf(':'));
    }

    @Override
    public Element getDomElement() {
        return readRange;
    }
}
