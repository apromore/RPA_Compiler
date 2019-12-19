package com.compiler.builder.UIPathActions.browser.navigation;

import com.compiler.builder.UIPathElement;
import com.compiler.builder.Variables;
import org.w3c.dom.Element;

public class OpenBrowser extends UIPathElement {
    private Element openBrowser;
    private Element activityAction;

    public OpenBrowser(String url) {
        openBrowser = doc.createElement("ui:OpenBrowser");
        setOpenBrowserAttributes(url);
        createBrowserBody();
    }

    private void setOpenBrowserAttributes(String url) {
        openBrowser.setAttribute("Url", url);
        openBrowser.setAttribute("BrowserType", "Chrome");
        openBrowser.setAttribute("DisplayName", "Open");
        openBrowser.setAttribute("Hidden", "False");
        openBrowser.setAttribute("NewSession", "True");
        openBrowser.setAttribute("Private", "False");
        openBrowser.setAttribute("UiBrowser", "[" + Variables.BROWSER + "]");
    }

    private void createBrowserBody() {
        Element openBrowserBody = doc.createElement("ui:OpenBrowser.Body");
        activityAction = doc.createElement("ActivityAction");
        activityAction.setAttribute("x:TypeArguments", "x:Object");

        Element activityActionArgument = doc.createElement("ActivityAction.Argument");
        Element delegateInArgument = doc.createElement("DelegateInArgument");
        delegateInArgument.setAttribute("x:TypeArguments", "x:Object");
        delegateInArgument.setAttribute("Name", "ContextTarget");

        openBrowser.appendChild(openBrowserBody).appendChild(activityAction)
                .appendChild(activityActionArgument).appendChild(delegateInArgument);
    }

    public Element getActivityAction() {
        return activityAction;
    }

    @Override
    public org.w3c.dom.Element getDomElement() {
        return openBrowser;
    }
}
