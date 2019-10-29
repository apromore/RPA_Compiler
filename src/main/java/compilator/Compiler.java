package compilator;

import compilator.entity.Action;
import org.w3c.dom.Element;

import java.util.List;

public class Compiler {
    private static List<Action> actions = Utils.readLog();
    private static Action previousAction;
    private static ScriptCreator scriptCreator;

    // Change to field of action (next version of logger)
    private static String excelFilePath = "C:\\Users\\dvsta\\Downloads\\StudentRecords.xlsx";

    public static void main(String[] args) {
        scriptCreator = new ScriptCreator();
        boolean newExcelScope = true;
        boolean newChromeScope = true;

        scriptCreator.createDoSequence();
        previousAction = actions.get(0);

        for (Action action : actions) {
            String currentApp = action.getTargetApp();

            if (newChromeScope && currentApp.equals("Chrome")) {
                Element openBrowser = ScriptCreator.createOpenBrowser(action.getUrl());
                openBrowser.appendChild(scriptCreator.createDoSequence());
                newChromeScope = false;
            } else if (newExcelScope && currentApp.equals("Excel")) {
                Element excelScope = ScriptCreator.createExcelScope(excelFilePath);
                excelScope.appendChild(scriptCreator.createDoSequence());
                newExcelScope = false;
            } else if (!currentApp.equals(previousAction.getTargetApp())) {
                Element doSequence = scriptCreator.createDoSequence();
                scriptCreator.setDoSequence(doSequence);
                Element scope = scriptCreator.getNewScope(currentApp).createScope(action);
                scope.appendChild(doSequence);
            }

            try {
                addElement(action);
            } catch (RuntimeException ex) {
                continue;
            }

            previousAction = action;
        }

        ScriptCreator.writeScript();
    }

    private static void addElement(Action action) {
        switch (action.getEventType()) {
            case "editField":
                if (previousAction.getEventType().equals("paste") &&
                        previousAction.getTargetName().equals(action.getTargetName())) {
                    throw new RuntimeException();
                }
                scriptCreator.addEditFieldElement(action.getTargetName(), action.getTargetValue());
                break;
            case "copy":
                scriptCreator.addCopyAction(action.getTargetName());
                break;
            case "paste":
                scriptCreator.addPasteAction(action.getTargetName());
                break;
            case "copyCell":
                scriptCreator.addCopyCell(action.getTargetId(), action.getSheetName());
                break;
            case "editCell":
                scriptCreator.addEditCellAction(action.getTargetId(), action.getSheetName(), action.getTargetValue());
                break;
            case "clickCheckbox":
                scriptCreator.addClickCheckboxAction(action.getTargetId());
                break;
            case "clickLink":
                scriptCreator.addClickLinkAction(action.getTargetHref(), action.getTargetInnerText(), action.getTargetClass());
                break;
            case "clickButton":
                scriptCreator.addClickButtonAction(action.getTargetClass(), action.getTargetInnerText(),
                        action.getTargetTagName(), action.getTargetType(), action.getTargetValue(), action.getTargetInnerHTML());
                break;
            case "pasteIntoCell":
                scriptCreator.addPasteIntoCellAction(action.getSheetName(), action.getTargetId());
                break;
            case "editRange":
                scriptCreator.addEditRangeAction(action.getSheetName(), action.getTargetId(), action.getTargetValue());
                break;
            case "copyRange":
                scriptCreator.addCopyRangeAction(action.getSheetName(), action.getTargetId());
                break;
            case "pasteIntoRange":
                scriptCreator.addPasteIntoRangeAction(action.getSheetName(), action.getTargetId());
                break;
            case "navigate_to":
                scriptCreator.addNavigateToAction(action.getUrl());
                break;
            case "createNewTab":
                scriptCreator.addCreateNewTabAction();
                break;
            case "selectTab":
                scriptCreator.addSelectTabAction(action.getTargetTitle());
        }
    }
}
