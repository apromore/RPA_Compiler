package com.compiler;

import com.compiler.builder.ScriptBuilder;
import com.compiler.entity.Action;

import java.util.List;

public class ImperativeCompiler {
    public static String TRANSFORMATIONS_FILE_NAME = "transformations.json";
    public static String LOG_FILE_PATH = "C:\\Foo\\work\\RPA_Compiler\\logs.csv";
    private static Action previousAction;
    private static ScriptBuilder scriptBuilder;
    private static boolean newExcelScope = true;
    private static boolean newChromeScope = true;
    // TODO: Change to field of action (next version of logger)
    private static String excelFilePath = "C:\\Users\\dvsta\\Downloads\\StudentRecords.xlsx";

    static void compile() {
        List<Action> actions = Utils.readLog();
        scriptBuilder = new ScriptBuilder();
        scriptBuilder.createDoSequence();
        parseActions(actions);
        ScriptBuilder.writeScript();
    }

    private static void parseActions(List<Action> actions) {
        previousAction = actions.get(0);
        for (Action action : actions) {
            createScope(action);
            if (!addElement(action)) {
                continue;
            }
            previousAction = action;
        }
    }

    private static void createScope(Action action) {
        String currentApp = action.getTargetApp();
        if (newChromeScope && currentApp.equals("Chrome")) {
            scriptBuilder.createOpenBrowser(action.getUrl(), scriptBuilder.createDoSequence());
            newChromeScope = false;
        } else if (newExcelScope && currentApp.equals("Excel")) {
            scriptBuilder.createExcelScope(excelFilePath, scriptBuilder.createDoSequence());
            newExcelScope = false;
        } else if (!currentApp.equals(previousAction.getTargetApp())) {
            scriptBuilder.createNewScope(action.getTargetApp(), scriptBuilder.createDoSequence(), excelFilePath);
        }
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
