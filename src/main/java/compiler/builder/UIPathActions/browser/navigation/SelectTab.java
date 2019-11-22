package compiler.builder.UIPathActions.browser.navigation;

import compiler.builder.ScriptBuilder;
import compiler.builder.UIPathElement;
import compiler.entity.Action;
import org.w3c.dom.Element;

public class SelectTab extends UIPathElement implements Tab {
    private Action action;
    private Element browserScope;

    public SelectTab(Action action, ScriptBuilder scriptBuilder) {
        this.action = action;
        createSelectTab(scriptBuilder);
    }

    private void createSelectTab(ScriptBuilder scriptBuilder) {
        browserScope = createBrowserScope(doc, action.getTargetTitle());
        createBrowserScopeBody(doc, browserScope, scriptBuilder);
    }

    @Override
    public Element getDomElement() {
        return browserScope;
    }
}
