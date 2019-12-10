package transformation;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Functions {
    private static int patternNum = 0;
    private static Map<String, Function<List<String>, String>> functions;

    static {
        functions = new HashMap<>();

        functions.put("f_split_w", Functions::fSplitW);
        functions.put("f_drop", Functions::fDrop);
        functions.put("f_split", Functions::fSplit);
        functions.put("f_join_char", Functions::fJoinChar);
        functions.put("f_split_first", Functions::fSplitFirst);
        functions.put("f_extract", Functions::fExtract);
    }

    static String getFunction(String function) {
        if (function.contains("pattern")) {
            return parseIfCondition(function);
        }
        List<String> args = getFunctionArgs(function);
        String functionName = getFunctionName(function);

        return functions.get(functionName).apply(args);
    }

    @SuppressWarnings("unchecked")
    private static String parseIfCondition(String function) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(function);
            JSONArray transformations = (JSONArray) json.get("transformations");
            String functions = (String) transformations.stream()
                    .reduce("", (t, f) -> t + Functions.getFunction((String) f));
            String pattern = (String) json.get("pattern");
            String condition = String.format("Regex.Match(t[0], @\"%s\").Success", pattern);

            if (patternNum == 0) {
                patternNum++;
                return String.format("if(%s){\n%s}\n", condition, functions);
            } else if (pattern.equals("default")) {
                return String.format("else {\n%s}\n", functions);
            } else {
                patternNum++;
                return String.format("else if(%s){\n%s}\n", condition, functions);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    static String assignTransformedValue() {
        return "transformed_value = String.Concat(t.ToArray());";
    }

    public static String getNewListFunction(String element) {
        return String.format("List<string> t = new List<string> { %s };\n", element);
    }

    private static String fSplitW(List<String> args) {
        int index = Integer.parseInt(args.get(1));

        return String.format("split = t[%d].Split(' ');\n" +
                "t.RemoveAt(%1$d);\n" +
                "t.InsertRange(%1$d, split);\n", index);
    }

    private static String fDrop(List<String> args) {
        int index = Integer.parseInt(args.get(1));

        return String.format("t.RemoveAt(%d);\n", index);
    }

    private static String fSplit(List<String> args) {
        int index = Integer.parseInt(args.get(1));
        String symbol = args.get(2);
        symbol = symbol.substring(1, symbol.length() - 1);

        return String.format("split = t[%d].Split('%s');\n" +
                "t.RemoveAt(%1$d);\n" +
                "t.InsertRange(%1$d, split);\n", index, symbol);
    }

    private static String fJoinChar(List<String> args) {
        int index = Integer.parseInt(args.get(1));
        String symbol = args.get(2);
        symbol = symbol.substring(1, symbol.length() - 1);

        return String.format("t[%d] = t[%1$d] + \"%s\" + t[%d];\n" +
                "t.RemoveAt(%3$d);\n", index, symbol, index + 1);
    }

    private static String fSplitFirst(List<String> args) {
        int index = Integer.parseInt(args.get(1));
        String symbol = args.get(2);
        symbol = symbol.substring(1, symbol.length() - 1);

        return String.format("split = t[%d].Split('%s');\n" +
                "t[%1$d] = split[0];\n" +
                "t.Insert(%d, String.Join(\"%2$s\", split.Skip(1).Take(split.Length - 1)).Trim());\n", index, symbol, index + 1);
    }

    private static String fExtract(List<String> args) {
        int index = Integer.parseInt(args.get(1));
        String regex = args.get(2);
        regex = regex.substring(1, regex.length() - 1);

        return String.format("t.Insert(%d, Regex.Match(t[%d], @\"%s\").Value);\n", index + 1, index, regex);
    }

    private static List<String> getFunctionArgs(String f) {
        List<String> args;
        Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(f);

        if (m.find()) {
            args = new ArrayList<>(Arrays.asList(m.group(1).split(",(?=(?:[^\']*\'[^\"]*\')*[^\']*$)")));
            args = args.stream().map(String::trim).collect(Collectors.toList());
        } else {
            throw new RuntimeException("Function " + f + "does not have parameters");
        }

        return args;
    }

    private static String getFunctionName(String f) {
        String functionName;
        Matcher m = Pattern.compile(".*(?=(\\())").matcher(f);

        if (m.find()) {
            functionName = m.group();
        } else {
            throw new RuntimeException("Function " + f + "does not have parameters");
        }

        return functionName;
    }
}
