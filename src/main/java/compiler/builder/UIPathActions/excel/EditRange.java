package compiler.builder.UIPathActions.excel;

import compiler.builder.UIPathElement;
import compiler.builder.Variables;
import compiler.entity.Action;
import org.w3c.dom.Element;

import javax.xml.transform.TransformerException;

public class EditRange extends UIPathElement implements ExcelRange {
    private Element writeRange;
    private Action action;

    public EditRange(Element doSequence, Action action) {
        this.action = action;
        createWriteRange(doSequence);
    }

    private void createWriteRange(Element doSequence) {
        buildDataTable(doSequence);
        writeRange = doc.createElement("ui:ExcelWriteRange");
        setAttributes(writeRange, action);
        doSequence.appendChild(writeRange);
    }

    private void buildDataTable(Element doSequence) {
        Element buildDataTable = doc.createElement("ui:BuildDataTable");
        try {
            TableInfo tableInfo = new TableInfo(action);
            buildDataTable.setAttribute("DisplayName", "Build Data Table");
            buildDataTable.setAttribute("DataTable", "[" + Variables.DATA_TABLE + "]");
            buildDataTable.setAttribute("TableInfo", tableInfo.getTableInfo());
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        doSequence.appendChild(buildDataTable);
    }

    @Override
    public Element getDomElement() {
        return writeRange;
    }
}
