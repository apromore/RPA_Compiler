package compiler.builder;

import compiler.builder.UIPathActions.browser.*;
import compiler.builder.UIPathActions.browser.navigation.*;
import compiler.builder.UIPathActions.excel.*;
import compiler.entity.Action;
import org.apache.commons.text.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ScriptBuilder {
    private static Document doc;
    private MainSequence mainSequence;
    private Element doSequence;
    private Map<String, Consumer<Action>> elements;

    public ScriptBuilder() {
        createNewDoc();
        createElementsMap();

        Activity activity = new Activity();
        Imports imports = new Imports();
        Variables variables = new Variables();
        mainSequence = new MainSequence();

        activity.appendChild(imports);
        activity.appendChild(mainSequence).appendChild(variables);
    }

    public static void writeScript() {
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            String output = writer.getBuffer().toString();
            BufferedWriter w = new BufferedWriter(new FileWriter("scripts/script.xaml"));
            w.write(StringEscapeUtils.unescapeXml(output));
            w.close();
            System.out.println("File saved!");
        } catch (TransformerException | IOException e) {
            e.printStackTrace();
        }
    }

    public void createOpenBrowser(String url, Element doSequence) {
        OpenBrowser openBrowser = new OpenBrowser(url);
        openBrowser.getActivityAction().appendChild(doSequence);
        mainSequence.appendChild(openBrowser);
    }

    public void createExcelScope(String filePath, Element doSequence) {
        ExcelScope excelScope = new ExcelScope(filePath);
        excelScope.getActivityAction().appendChild(doSequence);
        mainSequence.appendChild(excelScope);
    }

    private void createBrowserScope(Element doSequence) {
        ChromeScope chromeScope = new ChromeScope();
        chromeScope.getActivityAction().appendChild(doSequence);
        mainSequence.appendChild(chromeScope);
    }

    private void createElementsMap() {
        elements = new HashMap<>();
        elements.put("editField", (action) -> new EditField(doSequence, action));
        elements.put("editCell", (action) -> new EditCell(doSequence, action));
        elements.put("editRange", (action) -> new EditRange(doSequence, action));
        elements.put("copy", (action) -> new Copy(doSequence, action));
        elements.put("copyCell", (action) -> new CopyCell(doSequence, action));
        elements.put("copyRange", (action) -> new CopyRange(doSequence, action));
        elements.put("paste", (action) -> new Paste(doSequence, action));
        elements.put("pasteIntoCell", (action) -> new PasteIntoCell(doSequence, action));
        elements.put("pasteIntoRange", (action) -> new PasteIntoRange(doSequence, action));
        elements.put("clickLink", (action) -> new ClickLink(doSequence, action));
        elements.put("clickButton", (action) -> new ClickButton(doSequence, action));
        elements.put("clickCheckbox", (action) -> new ClickCheckbox(doSequence, action));
        elements.put("navigate_to", (action) -> new NavigateTo(doSequence, action));
        elements.put("createNewTab", (action) -> {
            NewTab newTab = new NewTab(doSequence, this);
            mainSequence.appendChild(newTab);
        });
        elements.put("selectTab", (action) -> {
            SelectTab selectTab = new SelectTab(action, this);
            mainSequence.appendChild(selectTab);
        });
    }

    private void createNewDoc() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.newDocument();

            UIPathElement.doc = doc;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public Element createDoSequence() {
        doSequence = doc.createElement("Sequence");
        doSequence.setAttribute("DisplayName", "Do");

        return doSequence;
    }

    public void createUIPathAction(Action action) {
        String actionName = action.getEventType();
        if (elements.containsKey(actionName))
            elements.get(actionName).accept(action);
    }

    public void createNewScope(Action action, Element doSequence, String filePath) {
        switch (action.getTargetApp()) {
            case "Excel":
                createExcelScope(filePath, doSequence);
                break;
            case "Chrome":
                createBrowserScope(doSequence);
                break;
            default:
                throw new IllegalArgumentException("Wrong application scope!");
        }
    }

    public void setDoSequence(Element doSequence) {
        this.doSequence = doSequence;
    }
}