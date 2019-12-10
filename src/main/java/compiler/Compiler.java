package compiler;

import compiler.builder.ScriptBuilder;
import compiler.entity.Action;

import java.util.List;

public class Compiler {
    private static Action previousAction;
    private static ScriptBuilder scriptBuilder;

    // TODO: Change to field of action (next version of logger)
    private static String excelFilePath = "C:\\Users\\dvsta\\Downloads\\StudentRecords.xlsx";

    public static void main(String[] args) {
        List<Action> actions = Utils.readLog();
        scriptBuilder = new ScriptBuilder();
        boolean newExcelScope = true;
        boolean newChromeScope = true;

        scriptBuilder.createDoSequence();
        previousAction = actions.get(0);

        for (Action action : actions) {
            String currentApp = action.getTargetApp();

            if (newChromeScope && currentApp.equals("Chrome")) {
                scriptBuilder.createOpenBrowser(action.getUrl(), scriptBuilder.createDoSequence());
                newChromeScope = false;
            } else if (newExcelScope && currentApp.equals("Excel")) {
                scriptBuilder.createExcelScope(excelFilePath, scriptBuilder.createDoSequence());
                newExcelScope = false;
            } else if (!currentApp.equals(previousAction.getTargetApp())) {
                scriptBuilder.createNewScope(action, scriptBuilder.createDoSequence(), excelFilePath);
            }

            if (!addElement(action)) {
                continue;
            }

            previousAction = action;
        }

        ScriptBuilder.writeScript();
    }

    private static boolean addElement(Action action) {
        String actionName = action.getEventType();
        String previousActionName = previousAction.getEventType();
        if (actionName.equals("editField") && previousActionName.equals("paste") &&
                previousAction.getTargetName().equals(action.getTargetName())) {
            return false;
        }
        scriptBuilder.createUIPathAction(action);

        return true;
    }
}
