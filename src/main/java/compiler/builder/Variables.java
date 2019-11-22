package compiler.builder;

import org.w3c.dom.Element;

public class Variables extends UIPathElement {
    public static final String CLIPBOARD = "clipboard";
    public static final String DATA_TABLE = "dataTable";
    public static final String BROWSER = "currentBrowser";
    private Element variables;

    public Variables() {
        variables = doc.createElement("Sequence.Variables");

        createClipboardVariable();
        createCurrentBrowserVariable();
        createDataTableVariable();
        createTransformedValue();
        createVariableT();
    }

    private void createClipboardVariable() {
        Element clipboardVariable = doc.createElement("Variable");
        clipboardVariable.setAttribute("Name", CLIPBOARD);
        clipboardVariable.setAttribute("x:TypeArguments", "x:String");

        variables.appendChild(clipboardVariable);
    }

    private void createCurrentBrowserVariable() {
        Element currentBrowserVariable = doc.createElement("Variable");
        currentBrowserVariable.setAttribute("Name", BROWSER);
        currentBrowserVariable.setAttribute("x:TypeArguments", "ui:Browser");

        variables.appendChild(currentBrowserVariable);
    }

    private void createDataTableVariable() {
        org.w3c.dom.Element dataTableVariable = doc.createElement("Variable");
        dataTableVariable.setAttribute("Name", DATA_TABLE);
        dataTableVariable.setAttribute("x:TypeArguments", "sd:DataTable");

        variables.appendChild(dataTableVariable);
    }

    private void createTransformedValue() {
        Element transformedValue = doc.createElement("Variable");
        transformedValue.setAttribute("Name", "transformed_value");
        transformedValue.setAttribute("x:TypeArguments", "x:String");

        variables.appendChild(transformedValue);
    }

    private void createVariableT() {
        Element t = doc.createElement("Variable");
        t.setAttribute("Name", "t");
        t.setAttribute("x:TypeArguments", "scg:List(x:String)");
        t.setAttribute("Default", "[new List(of string)]");

        variables.appendChild(t);
    }

    @Override
    public Element getDomElement() {
        return variables;
    }
}
