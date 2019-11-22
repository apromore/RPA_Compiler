package compiler.builder.UIPathActions.browser.navigation;

import compiler.builder.UIPathElement;
import compiler.builder.Variables;
import org.w3c.dom.Element;

public class ChromeScope extends UIPathElement {
    private Element browserScope;
    private Element activityAction;

    public ChromeScope() {
        createBrowserScope();
    }

    private void createBrowserScope() {
        browserScope = doc.createElement("ui:BrowserScope");
        setScopeAttributes();
        createScopeBody();
    }

    private void setScopeAttributes() {
        browserScope.setAttribute("Browser", "[" + Variables.BROWSER + "]");
        browserScope.setAttribute("BrowserType", "Chrome");
        browserScope.setAttribute("DisplayName", "Attach Browser Chrome");
    }

    private void createScopeBody() {
        Element browserScopeBody = doc.createElement("ui:BrowserScope.Body");
        activityAction = doc.createElement("ActivityAction");
        activityAction.setAttribute("x:TypeArguments", "x:Object");

        Element activityActionArgument = doc.createElement("ActivityAction.Argument");
        Element delegateInArgument = doc.createElement("DelegateInArgument");
        delegateInArgument.setAttribute("x:TypeArguments", "x:Object");
        delegateInArgument.setAttribute("Name", "ContextTarget");

        browserScope.appendChild(browserScopeBody).appendChild(activityAction)
                .appendChild(activityActionArgument).appendChild(delegateInArgument);
    }

    public Element getActivityAction() {
        return activityAction;
    }

    @Override
    public Element getDomElement() {
        return browserScope;
    }
}
