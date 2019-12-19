package com.compiler.builder.UIPathActions.excel;

import com.compiler.builder.Variables;
import com.compiler.entity.Action;
import org.w3c.dom.Element;


public interface ExcelRange {
    default void setAttributes(Element writeRange, Action action) {
        String sheetName = action.getSheetName();
        String firstCell = getFirstCell(action.getTargetId());
        writeRange.setAttribute("AddHeaders", "False");
        writeRange.setAttribute("DataTable", "[" + Variables.DATA_TABLE + "]");
        writeRange.setAttribute("DisplayName", "Write Range");
        writeRange.setAttribute("SheetName", sheetName);
        writeRange.setAttribute("StartingCell", firstCell);
    }

    private String getFirstCell(String range) {
        return range.substring(0, range.indexOf(':'));
    }
}
