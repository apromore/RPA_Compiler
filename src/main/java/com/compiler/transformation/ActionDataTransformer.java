package com.compiler.transformation;

import com.compiler.ImperativeCompiler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActionDataTransformer extends DataTransformer {

    public ActionDataTransformer() {
        fileName = ImperativeCompiler.TRANSFORMATIONS_FILE_NAME;
        transformations = new HashMap<>();
        ReadDataTransformationFromFile();
    }


    @SuppressWarnings("unchecked")
    void parseJSONTransformations(JSONArray dataTransformationList) {
        dataTransformationList.forEach(transformation -> {
            JSONObject transformationObject = ((JSONObject) transformation);
            String id = transformationObject.get("id").toString();
            List<String> instructions = new ArrayList<>();
            for (var t : (JSONArray) transformationObject.get("data_transformation")) {
                instructions.add(String.valueOf(t));
            }
            transformations.put(id, instructions);
        });
    }

}
