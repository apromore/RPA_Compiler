package compilator;

import com.opencsv.*;
import compilator.entity.Action;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    private static String filePath = "C:\\Foo\\work\\compilator\\logs.csv";

    public static void writeDataLineByLine(String filePath, String data) {
        File file = new File(filePath);
        try {
            FileWriter outputFile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputFile);

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Action> readLog() {
        List<Action> actions = new ArrayList<>();

        try {
            RFC4180Parser rfc4180Parser = new RFC4180ParserBuilder().build();
            BufferedReader filereader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8));
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withSkipLines(1)
                    .withCSVParser(rfc4180Parser)
                    .build();

            String[] line;
            while ((line = csvReader.readNext()) != null) {
                actions.add(Action.createActionFromArray(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return actions;
    }
}
