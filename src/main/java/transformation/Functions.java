package transformation;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Functions {

    private static Map<String, Function<List<String>, String>> functions;

    static {
        functions = new HashMap<>();

        functions.put("f_split_w", Functions::fSplitW);
        functions.put("f_drop", Functions::fDrop);
        functions.put("f_split", Functions::fSplit);
    }

    static String getFunction(String function) {
        List<String> args = getFunctionArgs(function);
        String functionName = getFunctionName(function);

        return functions.get(functionName).apply(args);
    }

    static String assignTransformedValue() {
        return "transformed_value = String.Concat(t.ToArray());";
    }

    public static String getNewListFunction(String element) {
        return "List<string> t = new List<string> { " + element + " };\n";
    }

    private static String fSplitW(List<String> args) {
        String index = args.get(1);

        return "string[] split = t[" + index + "].Split(' ');\n" +
                "t.RemoveAt(" + index + ");\n" +
                "t.InsertRange(" + index + ", split);\n";
    }

    private static String fDrop(List<String> args) {
        String index = args.get(1);

        return "t.RemoveAt(" + index + ");\n";
    }

    private static String fSplit(List<String> args) {
        String index = args.get(1);
        String symbol = args.get(2);

        return "string[] split = t[" + index + "].Split('" + symbol + "');\n" +
                "t.RemoveAt(" + index + ");\n" +
                "t.InsertRange(" + index + ", split);\n";
    }

    private static List<String> getFunctionArgs(String f) {
        List<String> args;
        Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(f);

        if (m.find()) {
            args = new ArrayList<>(Arrays.asList(m.group(1).split(",")));
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
