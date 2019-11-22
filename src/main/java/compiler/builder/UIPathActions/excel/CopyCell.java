package compiler.builder.UIPathActions.excel;

import compiler.builder.UIPathElement;
import compiler.builder.Variables;
import compiler.entity.Action;
import org.w3c.dom.Element;

public class CopyCell extends UIPathElement {
    private Element excelReadCell;
    private Action action;

    public CopyCell(Element doSequence, Action action) {
        this.action = action;
        createCopyCell(doSequence);
    }

    private void createCopyCell(Element doSequence) {
        excelReadCell = doc.createElement("ui:ExcelReadCell");
        setCopyCellAttributes();
        createResult();
        doSequence.appendChild(excelReadCell);
    }

    private void createResult() {
        Element excelReadCellResult = doc.createElement("ui:ExcelReadCell.Result");
        Element outArgument = doc.createElement("OutArgument");
        outArgument.setAttribute("x:TypeArguments", "x:String");
        outArgument.setTextContent("[" + Variables.CLIPBOARD + "]");

        excelReadCell.appendChild(excelReadCellResult).appendChild(outArgument);
    }

    private void setCopyCellAttributes() {
        excelReadCell.setAttribute("Cell", action.getTargetId());
        excelReadCell.setAttribute("DisplayName", "Read Cell");
        excelReadCell.setAttribute("SheetName", action.getSheetName());
    }

    @Override
    public Element getDomElement() {
        return excelReadCell;
    }
}
