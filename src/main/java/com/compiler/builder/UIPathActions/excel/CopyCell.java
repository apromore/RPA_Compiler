package com.compiler.builder.UIPathActions.excel;

import com.compiler.builder.Arguments;
import com.compiler.builder.UIPathElement;
import com.compiler.builder.Variables;
import com.compiler.entity.Action;
import org.w3c.dom.Element;

public class CopyCell extends UIPathElement {
    private boolean isDeclarativeMode;
    private Element excelReadCell;
    private Action action;

    public CopyCell(Action action) {
        this.action = action;
        this.isDeclarativeMode = action.isInDeclarativeMode();
        createCopyCell(doSequence);
    }

    private void createCopyCell(Element doSequence) {
        excelReadCell = doc.createElement("ui:ExcelReadCell");
        setCopyCellAttributes();
        createResult();
        doSequence.appendChild(excelReadCell);
        if (isDeclarativeMode) {
            doSequence.appendChild(createAddToCollectionAction());
        }
    }

    private void createResult() {
        Element excelReadCellResult = doc.createElement("ui:ExcelReadCell.Result");
        Element outArgument = doc.createElement("OutArgument");
        outArgument.setAttribute("x:TypeArguments", "x:String");
        outArgument.setTextContent("[" + Variables.CLIPBOARD + "]");

        excelReadCell.appendChild(excelReadCellResult).appendChild(outArgument);
    }

    private void setCopyCellAttributes() {
        if (isDeclarativeMode) {
            if (action.isDeclaredColumn()) {
                String cell = "[&quot;" + action.getTargetId() + "&quot; +" + Arguments.ROW + "]";
                excelReadCell.setAttribute("Cell", cell);
            } else {
                String cell = "[" + Arguments.COLUMN + " + &quot;" + action.getTargetId() + "&quot;]";
                excelReadCell.setAttribute("Cell", cell);
            }
        } else {
            excelReadCell.setAttribute("Cell", action.getTargetId());
        }
        excelReadCell.setAttribute("DisplayName", "Read Cell");
        excelReadCell.setAttribute("SheetName", action.getSheetName());
    }

    private Element createAddToCollectionAction() {
        Element addToCollection = doc.createElement("AddToCollection");
        addToCollection.setAttribute("DisplayName", "Add To Collection");
        addToCollection.setAttribute("x:TypeArguments", "x:String");
        addToCollection.setAttribute("Collection", "[" + Variables.CELLS + "]");
        addToCollection.setAttribute("Item", "[" + Variables.CLIPBOARD + "]");

        return addToCollection;
    }

    @Override
    public Element getDomElement() {
        return excelReadCell;
    }
}
