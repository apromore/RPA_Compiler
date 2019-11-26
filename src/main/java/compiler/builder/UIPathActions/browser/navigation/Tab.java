package compiler.builder.UIPathActions.browser.navigation;

import compiler.builder.ScriptBuilder;
import compiler.builder.Variables;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface Tab {
    String NEW_TAB_TITLE = "New Tab*";

    default void createBrowserScopeBody(Document doc, Element browserScope, ScriptBuilder scriptBuilder) {
        Element browserScopeBody = doc.createElement("ui:BrowserScope.Body");
        Element activityAction = doc.createElement("ActivityAction");
        Element activityAction_Argument = doc.createElement("ActivityAction.Argument");
        Element delegateInArgument = doc.createElement("DelegateInArgument");
        Element sequence = doc.createElement("Sequence");

        activityAction.setAttribute("x:TypeArguments", "x:Object");
        delegateInArgument.setAttribute("x:TypeArguments", "x:Object");
        delegateInArgument.setAttribute("Name", "ContextTarget");
        sequence.setAttribute("DisplayName", "Do");

        scriptBuilder.setDoSequence(sequence);

        activityAction.appendChild(activityAction_Argument).appendChild(delegateInArgument);
        browserScopeBody.appendChild(activityAction).appendChild(sequence);
        browserScope.appendChild(browserScopeBody);
    }

    default Element createBrowserScope(Document doc, String title) {
        Element browserScope = doc.createElement("ui:BrowserScope");
        browserScope.setAttribute("BrowserType", "Chrome");
        browserScope.setAttribute("DisplayName", "Attach Browser");
        browserScope.setAttribute("Selector", "&lt;html app='chrome.exe' title='" + title + "'/&gt;");
        browserScope.setAttribute("UiBrowser", "[" + Variables.BROWSER + "]");
        return browserScope;
    }
}
