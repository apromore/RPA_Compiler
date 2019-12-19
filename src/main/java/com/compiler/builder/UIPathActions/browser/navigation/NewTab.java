package com.compiler.builder.UIPathActions.browser.navigation;

import com.compiler.builder.ScriptBuilder;
import com.compiler.builder.UIPathElement;
import org.w3c.dom.Element;

public class NewTab extends UIPathElement implements Tab {
    private Element browserScope;

    public NewTab(Element doSequence, ScriptBuilder scriptBuilder) {
        createSendHotKey(doSequence);
        browserScope = createBrowserScope(doc, NEW_TAB_TITLE);
        createBrowserScopeBody(doc, browserScope, scriptBuilder);
    }

    private void createSendHotKey(Element doSequence) {
        Element sendHotKey = doc.createElement("ui:SendHotkey");
        sendHotKey.setAttribute("Activate", "True");
        sendHotKey.setAttribute("DisplayName", "Create New Tab");
        sendHotKey.setAttribute("Key", "t");
        sendHotKey.setAttribute("KeyModifiers", "Ctrl");

        doSequence.appendChild(sendHotKey);
    }

    @Override
    public Element getDomElement() {
        return browserScope;
    }
}
