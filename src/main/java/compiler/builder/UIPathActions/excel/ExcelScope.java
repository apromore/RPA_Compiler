package compiler.builder.UIPathActions.excel;

import compiler.builder.UIPathElement;
import org.w3c.dom.Element;

public class ExcelScope extends UIPathElement {
    private Element excelScope;
    private Element activityAction;

    public ExcelScope(String filePath) {
        createExcelScope(filePath);
    }

    private void createExcelScope(String filePath) {
        excelScope = doc.createElement("ui:ExcelApplicationScope");
        setExcelScopeAttributes(filePath);
        createScopeBody();
    }

    private void setExcelScopeAttributes(String filePath) {
        excelScope.setAttribute("DisplayName", "Excel Application compilator.scope.Scope");
        excelScope.setAttribute("WorkbookPath", filePath);
    }

    private void createScopeBody() {
        Element excelApplicationScopeBody = doc.createElement("ui:ExcelApplicationScope.Body");
        activityAction = doc.createElement("ActivityAction");
        activityAction.setAttribute("x:TypeArguments", "ui:WorkbookApplication");

        Element activityActionArgument = doc.createElement("ActivityAction.Argument");

        Element delegateInArgument = doc.createElement("DelegateInArgument");
        delegateInArgument.setAttribute("x:TypeArguments", "ui:WorkbookApplication");
        delegateInArgument.setAttribute("Name", "ExcelWorkbookScope");

        excelScope.appendChild(excelApplicationScopeBody).appendChild(activityAction)
                .appendChild(activityActionArgument).appendChild(delegateInArgument);
    }

    public Element getActivityAction() {
        return activityAction;
    }

    @Override
    public Element getDomElement() {
        return excelScope;
    }
}
