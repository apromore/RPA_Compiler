package com.compiler;

import com.compiler.builder.ScriptBuilder;
import com.compiler.entity.Action;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class DeclarativeCompiler {
    public static String FILE_NAME = "transformations.json";
    private static ScriptBuilder scriptBuilder;
    private static boolean[] newExcelScope = {true};
    private static boolean[] newChromeScope = {true};
    private static String[] previousApp = {""};

    static void compile() {
        scriptBuilder = new ScriptBuilder();
        scriptBuilder.createDoSequence();
        ReadDataTransformationFromFile();
        ScriptBuilder.writeScript();
    }

    private static void ReadDataTransformationFromFile() {
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(FILE_NAME)) {
            Object obj = jsonParser.parse(reader);
            parseJSONTransformations((JSONArray) obj);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private static void parseJSONTransformations(JSONArray dataTransformationList) {
        dataTransformationList.forEach(t -> {
            JSONObject transformation = (JSONObject) t;
            createSourceScope(transformation);
            createTargetScope(transformation);
        });
    }

    private static void createSourceScope(JSONObject transformation) {
        String sourceApp = transformation.get("sourceApp").toString();
        String sourceURL = transformation.get("sourceURL").toString();
        createScope(sourceApp, sourceURL);
        addSourceElement(transformation, sourceApp);
    }

    private static void createTargetScope(JSONObject transformation) {
        String targetApp = transformation.get("targetApp").toString();
        String targetURL = transformation.get("targetURL").toString();
        createScope(targetApp, targetURL);
        addTargetElement(transformation, targetApp);
    }

    private static void createScope(String app, String url) {
        if (newChromeScope[0] && app.equals("Chrome")) {
            scriptBuilder.createOpenBrowser(url, scriptBuilder.createDoSequence());
            newChromeScope[0] = false;
        } else if (newExcelScope[0] && url.equals("Excel")) {
            scriptBuilder.createExcelScope(url, scriptBuilder.createDoSequence());
            newExcelScope[0] = false;
        } else if (!app.equals(previousApp[0])) {
            scriptBuilder.createNewScope(app, scriptBuilder.createDoSequence(), url);
        }
        previousApp[0] = app;
    }

    private static void addSourceElement(JSONObject transformation, String app) {
        Action action = new Action();
        action.setInDeclarativeMode(true);
        if (app.equals("Chrome")) {
            action.setEventType("copy");
            action.setTargetName(transformation.get("source").toString());
        } else if (app.equals("Excel")) {
            action.setEventType("copyCell");
            JSONObject obj = (JSONObject) transformation.get("source");
            for (var cell : (JSONArray) obj.get("cell")) {
                action.setTargetId(cell.toString());
                action.setSheetName(obj.get("sheet").toString());
                scriptBuilder.createUIPathAction(action);
            }
        }
    }

    private static void addTargetElement(JSONObject transformation, String app) {
        Action action = new Action();
        action.setInDeclarativeMode(true);
        if (app.equals("Chrome")) {
            action.setEventType("paste");
            action.setTargetName(transformation.get("target").toString());
        } else if (app.equals("Excel")) {
            action.setEventType("pasteIntoCell");
            JSONObject obj = (JSONObject) transformation.get("target");
            action.setTargetId(obj.get("cell").toString());
            action.setSheetName(obj.get("sheet").toString());
        }
        scriptBuilder.createUIPathAction(action);
    }
}
