package transformation;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataTransformer {
    private static final String FILE_NAME = "transformation.json";
    private Map<String, List<String>> transformations;

    public DataTransformer() {
        transformations = new HashMap<>();
        ReadDataTransformationFromFile();
    }

    public void ReadDataTransformationFromFile() {
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(FILE_NAME)) {
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

    @SuppressWarnings("unchecked")
    private void parseJSONTransformations(JSONArray dataTransformationList) {
        dataTransformationList.forEach(transformation -> {
            JSONObject transformationObject = ((JSONObject) transformation);
            String id = transformationObject.get("id").toString();
            List<String> instructions = (List<String>) transformationObject.get("data_transformation");
            transformations.put(id, instructions);
        });
    }

    public boolean isDataTransformationPresent(String id) {
        return transformations.containsKey(id);
    }
}
