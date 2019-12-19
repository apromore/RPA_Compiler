package com.compiler.transformation;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class DataTransformer {
    Map<String, List<String>> transformations;
    String fileName;

    public void ReadDataTransformationFromFile() {
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(fileName)) {
            Object obj = jsonParser.parse(reader);
            parseJSONTransformations((JSONArray) obj);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public String createTransformation(String id) {
        return transformations.get(id)
                .stream()
                .reduce("", (t, f) -> t + Functions.getFunction(f)) +
                Functions.assignTransformedValue();
    }

    public boolean isDataTransformationPresent(String id) {
        return transformations.containsKey(id);
    }

    abstract void parseJSONTransformations(JSONArray dataTransformationList);
}
