package com.compiler.builder.UIPathActions.browser.navigation;

import com.compiler.builder.UIPathElement;
import com.compiler.entity.Action;
import org.w3c.dom.Element;

public class NavigateTo extends UIPathElement {
    private Element navigateTo;
    private Action action;

    public NavigateTo(Element doSequence, Action action) {
        this.action = action;
        createNavigate(doSequence);
    }

    private void createNavigate(Element doSequence) {
        navigateTo = doc.createElement("ui:NavigateTo");
        setAttributes();
        doSequence.appendChild(navigateTo);
    }

    private void setAttributes() {
        navigateTo.setAttribute("DisplayName", "Navigate To");
        navigateTo.setAttribute("Url", action.getUrl());
    }

    @Override
    public Element getDomElement() {
        return navigateTo;
    }
}
