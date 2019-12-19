package com.compiler.builder.UIPathActions.browser;

import com.compiler.builder.UIPathElement;
import com.compiler.entity.Action;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ClickButton extends UIPathElement {
    private Element click;
    private Action action;

    public ClickButton(Element doSequence, Action action) {
        this.action = action;
        createClick(doSequence);
    }

    private void createClick(Element doSequence) {
        click = doc.createElement("ui:Click");
        setClickAttributes();
        createTarget();
        doSequence.appendChild(click);
    }

    private void createTarget() {
        Element clickTarget = doc.createElement("ui:Click.Target");
        Element target = doc.createElement("ui:Target");
        String selectors = getSelectors();
        target.setAttribute("Selector", "&lt;webctrl " + selectors + "/&gt;");
        target.setAttribute("WaitForReady", "COMPLETE");

        click.appendChild(clickTarget).appendChild(target);
    }

    private void setClickAttributes() {
        click.setAttribute("ClickType", "CLICK_SINGLE");
        click.setAttribute("DisplayName", "Click BUTTON");
        click.setAttribute("KeyModifiers", "None");
        click.setAttribute("MouseButton", "BTN_LEFT");
        click.setAttribute("SendWindowMessages", "False");
        click.setAttribute("SimulateClick", "True");
    }

    private String getSelectors() {
        Map<String, String> selectorsMap = new HashMap<>();
        selectorsMap.put("tag", "'" + action.getTargetTagName() + "'");
        selectorsMap.put("title", "'" + action.getTargetTitle() + "'");
        selectorsMap.put("id", "'" + action.getTargetId() + "'");
        selectorsMap.put("innerText", "'" + " ".repeat(getPrefix()) + action.getTargetInnerText() + " ".repeat(getSuffix()) + "'");

        if (action.getTargetTagName().equals("input")) {
            selectorsMap.put("type", "'" + action.getTargetType() + "'");
            selectorsMap.put("aaname", "'" + " ".repeat(getPrefix()) + action.getTargetValue() + " ".repeat(getSuffix()) + "'");
        }

        return getSelectorsFromMap(selectorsMap);
    }

    private int getPrefix() {
        String innerHTML = action.getTargetInnerHTML();

        return StringUtils.countMatches(innerHTML.substring(0, innerHTML.indexOf('<')), " ") +
                StringUtils.countMatches(innerHTML.substring(0, innerHTML.indexOf('<')), "\\n");
    }

    private int getSuffix() {
        String innerHTML = action.getTargetInnerHTML();

        return StringUtils.countMatches(innerHTML.substring(innerHTML.lastIndexOf('>') + 1), " ") +
                StringUtils.countMatches(innerHTML.substring(innerHTML.lastIndexOf('>') + 1), "\\n");
    }

    private String getSelectorsFromMap(Map<String, String> map) {
        return map.entrySet().stream()
                .filter(e -> !e.getValue().equals("''"))
                .map(Object::toString)
                .collect(Collectors.joining(" "));
    }

    @Override
    public Element getDomElement() {
        return click;
    }
}
