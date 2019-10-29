package compilator;

import compilator.scope.ChromeScope;
import compilator.scope.ExcelScope;
import compilator.scope.Scope;
import org.apache.commons.lang3.StringUtils;
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
import java.util.*;
import java.util.stream.Collectors;

public class ScriptCreator {
    private static Document doc;
    private static Element mainSequence;
    private static Element openBrowser;
    private Element doSequence;

    public ScriptCreator() {
        createMainScript();
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

    public static Element createOpenBrowser(String url) {
        openBrowser = doc.createElement("ui:OpenBrowser");
        openBrowser.setAttribute("BrowserType", "Chrome");
        openBrowser.setAttribute("DisplayName", "Open");
        openBrowser.setAttribute("Hidden", "False");
        openBrowser.setAttribute("NewSession", "True");
        openBrowser.setAttribute("Private", "False");
        openBrowser.setAttribute("Url", url);
        openBrowser.setAttribute("UiBrowser", "[currentBrowser]");

        Element openBrowserBody = doc.createElement("ui:OpenBrowser.Body");

        Element activityAction = doc.createElement("ActivityAction");
        activityAction.setAttribute("x:TypeArguments", "x:Object");

        Element activityAction_Argument = doc.createElement("ActivityAction.Argument");
        activityAction.appendChild(activityAction_Argument);

        Element delegateInArgument = doc.createElement("DelegateInArgument");
        delegateInArgument.setAttribute("x:TypeArguments", "x:Object");
        delegateInArgument.setAttribute("Name", "ContextTarget");

        activityAction_Argument.appendChild(delegateInArgument);
        openBrowserBody.appendChild(activityAction);
        openBrowser.appendChild(openBrowserBody);
        mainSequence.appendChild(openBrowser);

        return activityAction;
    }

    public static Element createExcelScope(String filePath) {
        Element excelApplicationScope = doc.createElement("ui:ExcelApplicationScope");
        excelApplicationScope.setAttribute("DisplayName", "Excel Application compilator.scope.Scope");
        excelApplicationScope.setAttribute("WorkbookPath", filePath);

        Element excelApplicationScopeBody = doc.createElement("ui:ExcelApplicationScope.Body");

        Element activityAction = doc.createElement("ActivityAction");
        activityAction.setAttribute("x:TypeArguments", "ui:WorkbookApplication");

        Element activityActionArgument = doc.createElement("ActivityAction.Argument");

        Element delegateInArgument = doc.createElement("DelegateInArgument");
        delegateInArgument.setAttribute("x:TypeArguments", "ui:WorkbookApplication");
        delegateInArgument.setAttribute("Name", "ExcelWorkbookScope");

        activityActionArgument.appendChild(delegateInArgument);
        activityAction.appendChild(activityActionArgument);
        excelApplicationScopeBody.appendChild(activityAction);
        excelApplicationScope.appendChild(excelApplicationScopeBody);
        mainSequence.appendChild(excelApplicationScope);

        return activityAction;
    }

    public static Element createBrowserScope() {
        Element browserScope = doc.createElement("ui:BrowserScope");
        browserScope.setAttribute("Browser", "[currentBrowser]");
        browserScope.setAttribute("BrowserType", "Chrome");
        browserScope.setAttribute("DisplayName", "Attach Browser Chrome");

        Element browserScopeBody = doc.createElement("ui:BrowserScope.Body");

        Element activityAction = doc.createElement("ActivityAction");
        activityAction.setAttribute("x:TypeArguments", "x:Object");

        Element activityActionArgument = doc.createElement("ActivityAction.Argument");

        Element delegateInArgument = doc.createElement("DelegateInArgument");
        delegateInArgument.setAttribute("x:TypeArguments", "x:Object");
        delegateInArgument.setAttribute("Name", "ContextTarget");

        activityActionArgument.appendChild(delegateInArgument);
        activityAction.appendChild(activityActionArgument);
        browserScopeBody.appendChild(activityAction);
        browserScope.appendChild(browserScopeBody);
        mainSequence.appendChild(browserScope);

        return activityAction;
    }

    public static Document getDoc() {
        return doc;
    }

    private void createMainScript() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.newDocument();

            Element rootElement = doc.createElement("Activity");
            rootElement.setAttribute("mc:Ignorable", "sap sap2010");
            rootElement.setAttribute("xmlns", "http://schemas.microsoft.com/netfx/2009/xaml/activities");
            rootElement.setAttribute("xmlns:mc", "http://schemas.openxmlformats.org/markup-compatibility/2006");
            rootElement.setAttribute("xmlns:mva", "clr-namespace:Microsoft.VisualBasic.Activities;assembly=System.Activities");
            rootElement.setAttribute("xmlns:sap", "http://schemas.microsoft.com/netfx/2009/xaml/activities/presentation");
            rootElement.setAttribute("xmlns:sap2010", "http://schemas.microsoft.com/netfx/2010/xaml/activities/presentation");
            rootElement.setAttribute("xmlns:scg", "clr-namespace:System.Collections.Generic;assembly=mscorlib");
            rootElement.setAttribute("xmlns:sco", "clr-namespace:System.Collections.ObjectModel;assembly=mscorlib");
            rootElement.setAttribute("xmlns:ui", "http://schemas.uipath.com/workflow/activities");
            rootElement.setAttribute("xmlns:x", "http://schemas.microsoft.com/winfx/2006/xaml");
            rootElement.setAttribute("xmlns:sd", "clr-namespace:System.Data;assembly=System.Data");

            mainSequence = doc.createElement("Sequence");
            mainSequence.setAttribute("DisplayName", "Main Sequence");

            Element sequenceVariables = doc.createElement("Sequence.Variables");

            Element clipboardVariable = doc.createElement("Variable");
            clipboardVariable.setAttribute("x:TypeArguments", "x:String");
            clipboardVariable.setAttribute("Name", "clipboard");

            Element currentBrowserVariable = doc.createElement("Variable");
            currentBrowserVariable.setAttribute("x:TypeArguments", "ui:Browser");
            currentBrowserVariable.setAttribute("Name", "currentBrowser");

            Element dataTableVariable = doc.createElement("Variable");
            dataTableVariable.setAttribute("x:TypeArguments", "sd:DataTable");
            dataTableVariable.setAttribute("Name", "dataTable");

            sequenceVariables.appendChild(currentBrowserVariable);
            sequenceVariables.appendChild(clipboardVariable);
            sequenceVariables.appendChild(dataTableVariable);
            mainSequence.appendChild(sequenceVariables);
            rootElement.appendChild(mainSequence);
            doc.appendChild(rootElement);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public Element createDoSequence() {
        doSequence = doc.createElement("Sequence");
        doSequence.setAttribute("DisplayName", "Do");

        return doSequence;
    }

    public void addEditFieldElement(String targetName, String targetValue) {
        Element typeInto = doc.createElement("ui:TypeInto");
        typeInto.setAttribute("Activate", "True");
        typeInto.setAttribute("ClickBeforeTyping", "False");
        typeInto.setAttribute("DisplayName", "Type Into 'INPUT'");
        typeInto.setAttribute("EmptyField", "False");
        typeInto.setAttribute("SendWindowMessages", "False");
        typeInto.setAttribute("SimulateType", "True");
        typeInto.setAttribute("Text", targetValue);

        Element typeIntoTarget = doc.createElement("ui:TypeInto.Target");

        Element target = doc.createElement("ui:Target");
        target.setAttribute("Selector", '&' + "lt;webctrl name='" + targetName + "' tag='INPUT' /&gt;");
        target.setAttribute("WaitForReady", "COMPLETE");

        typeIntoTarget.appendChild(target);
        typeInto.appendChild(typeIntoTarget);
        doSequence.appendChild(typeInto);
    }

    public void addCopyAction(String targetName) {
        Element getValue = doc.createElement("ui:GetValue");
        getValue.setAttribute("DisplayName", "Get Text 'INPUT'");

        Element getValueTarget = doc.createElement("ui:GetValue.Target");

        Element target = doc.createElement("ui:Target");
        target.setAttribute("Selector", "&lt;webctrl name='" + targetName + "' tag='INPUT' /&gt;");
        target.setAttribute("WaitForReady", "INTERACTIVE");

        Element getValueValue = doc.createElement("ui:GetValue.Value");

        Element outArgument = doc.createElement("OutArgument");
        outArgument.setAttribute("x:TypeArguments", "x:String");
        outArgument.setTextContent("[clipboard]");

        getValueValue.appendChild(outArgument);
        getValue.appendChild(getValueValue);
        getValueTarget.appendChild(target);
        getValue.appendChild(getValueTarget);
        doSequence.appendChild(getValue);
    }

    public void addPasteAction(String targetName) {
        Element typeInto = doc.createElement("ui:TypeInto");
        typeInto.setAttribute("Activate", "True");
        typeInto.setAttribute("ClickBeforeTyping", "False");
        typeInto.setAttribute("DisplayName", "Type Into 'INPUT'");
        typeInto.setAttribute("EmptyField", "False");
        typeInto.setAttribute("SendWindowMessages", "False");
        typeInto.setAttribute("SimulateType", "True");
        typeInto.setAttribute("Text", "[clipboard]");

        Element typeIntoTarget = doc.createElement("ui:TypeInto.Target");

        Element target = doc.createElement("ui:Target");
        target.setAttribute("Selector", "&lt;webctrl name='" + targetName + "' tag='INPUT' /&gt;");
        target.setAttribute("WaitForReady", "COMPLETE");

        typeIntoTarget.appendChild(target);
        typeInto.appendChild(typeIntoTarget);
        doSequence.appendChild(typeInto);
    }

    public void addCopyCell(String cell, String sheetName) {
        Element excelReadCell = doc.createElement("ui:ExcelReadCell");
        excelReadCell.setAttribute("Cell", cell);
        excelReadCell.setAttribute("DisplayName", "Read Cell");
        excelReadCell.setAttribute("SheetName", sheetName);

        Element excelReadCellResult = doc.createElement("ui:ExcelReadCell.Result");

        Element outArgument = doc.createElement("OutArgument");
        outArgument.setAttribute("x:TypeArguments", "x:String");
        outArgument.setTextContent("[clipboard]");

        excelReadCellResult.appendChild(outArgument);
        excelReadCell.appendChild(excelReadCellResult);
        doSequence.appendChild(excelReadCell);
    }

    public void addClickCheckboxAction(String target_id) {
        Element check = doc.createElement("ui:Check");
        check.setAttribute("compilator.entity.Action", "Check");
        check.setAttribute("DisplayName", "Check");

        Element checkTarget = doc.createElement("ui:Check.Target");

        Element target = doc.createElement("ui:Target");
        target.setAttribute("Selector", "&lt;webctrl id='" + target_id + "' tag='INPUT' /&gt;");
        target.setAttribute("WaitForReady", "COMPLETE");

        checkTarget.appendChild(target);
        doSequence.appendChild(check);
    }

    public void addClickLinkAction(String targetHref, String targetInnerText, String targetClass) {
        Element click = doc.createElement("ui:Click");
        click.setAttribute("ClickType", "CLICK_SINGLE");
        click.setAttribute("DisplayName", "Click 'A  " + targetHref + "'");
        click.setAttribute("KeyModifiers", "None");
        click.setAttribute("MouseButton", "BTN_LEFT");
        click.setAttribute("SendWindowMessages", "False");
        click.setAttribute("SimulateClick", "True");

        Element clickTarget = doc.createElement("ui:Click.Target");

        Element target = doc.createElement("ui:Target");
        target.setAttribute("Selector", "&lt;webctrl innerText='" + targetInnerText +
                "' tag='A' href='" + targetHref + "' class='" + targetClass + "'/&gt;");
        target.setAttribute("WaitForReady", "COMPLETE");

        clickTarget.appendChild(target);
        click.appendChild(clickTarget);
        doSequence.appendChild(click);
    }

    public void addClickButtonAction(String target_class, String target_innerText,
                                     String target_tagName, String target_type,
                                     String target_value, String targetInnerHTML) {
        int prefix = StringUtils.countMatches(targetInnerHTML.substring(0, targetInnerHTML.indexOf('<')), " ") +
                StringUtils.countMatches(targetInnerHTML.substring(0, targetInnerHTML.indexOf('<')), "\\n");
        int suffix = StringUtils.countMatches(targetInnerHTML.substring(targetInnerHTML.lastIndexOf('>') + 1), " ") +
                StringUtils.countMatches(targetInnerHTML.substring(targetInnerHTML.lastIndexOf('>') + 1), "\\n");

        Element click = doc.createElement("ui:Click");
        click.setAttribute("ClickType", "CLICK_SINGLE");
        click.setAttribute("DisplayName", "Click BUTTON");
        click.setAttribute("KeyModifiers", "None");
        click.setAttribute("MouseButton", "BTN_LEFT");
        click.setAttribute("SendWindowMessages", "False");
        click.setAttribute("SimulateClick", "True");

        Element clickTarget = doc.createElement("ui:Click.Target");

        Element target = doc.createElement("ui:Target");

        Map<String, String> map = new HashMap<>();
        map.put("tag", "'" + target_tagName + "'");
        map.put("class", "'" + target_class + "'");
        map.put("innerText", "'" + " ".repeat(prefix) + target_innerText + " ".repeat(suffix) + "'");
        if (target_tagName.equals("input")) {
            map.put("type", "'" + target_type + "'");
            map.put("aaname", "'" + " ".repeat(prefix) + target_value + " ".repeat(suffix) + "'");
        }
        String selectors = map.entrySet().stream()
                .filter(e -> !e.getValue().equals("''"))
                .map(Object::toString)
                .collect(Collectors.joining(" "));
        target.setAttribute("Selector", "&lt;webctrl " + selectors + "/&gt;");
        target.setAttribute("WaitForReady", "COMPLETE");

        clickTarget.appendChild(target);
        click.appendChild(clickTarget);
        doSequence.appendChild(click);
    }

    public void addEditCellAction(String cell, String sheetName, String text) {
        Element excelWriteCell = doc.createElement("ui:ExcelWriteCell");
        excelWriteCell.setAttribute("Cell", cell);
        excelWriteCell.setAttribute("DisplayName", "Write Cell");
        excelWriteCell.setAttribute("SheetName", sheetName);
        excelWriteCell.setAttribute("Text", text);

        doSequence.appendChild(excelWriteCell);
    }

    public void addPasteIntoCellAction(String sheetName, String cell) {
        Element excelWriteCell = doc.createElement("ui:ExcelWriteCell");
        excelWriteCell.setAttribute("DisplayName", "Paste Cell");
        excelWriteCell.setAttribute("Cell", cell);
        excelWriteCell.setAttribute("SheetName", sheetName);
        excelWriteCell.setAttribute("Text", "[clipboard]");

        doSequence.appendChild(excelWriteCell);
    }

    public Scope getNewScope(String targetApp) {
        switch (targetApp) {
            case "Excel":
                return new ExcelScope();
            case "Chrome":
                return new ChromeScope();
            default:
                throw new IllegalArgumentException("Wrong application compilator.scope.Scope!");
        }
    }

    public Element getDoSequence() {
        return doSequence;
    }

    public void setDoSequence(Element doSequence) {
        this.doSequence = doSequence;
    }

    public void addEditRangeAction(String sheetName, String range, String value) {
        addBuildDataTable(value);

        Element excelWriteRange = doc.createElement("ui:ExcelWriteRange");
        excelWriteRange.setAttribute("AddHeaders", "False");
        excelWriteRange.setAttribute("DataTable", "[dataTable]");
        excelWriteRange.setAttribute("DisplayName", "Write Range");
        excelWriteRange.setAttribute("SheetName", sheetName);
        excelWriteRange.setAttribute("StartingCell", getFirstCell(range));

        doSequence.appendChild(excelWriteRange);
    }

    private void addBuildDataTable(String value) {
        Element buildDataTable = doc.createElement("ui:BuildDataTable");
        try {
            buildDataTable.setAttribute("DisplayName", "Build Data Table");
            buildDataTable.setAttribute("DataTable", "[dataTable]");
            buildDataTable.setAttribute("TableInfo", createTableInfo(value));
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }

        doSequence.appendChild(buildDataTable);
    }

    private String createTableInfo(String value) throws ParserConfigurationException, TransformerException {
        //For testing
//        value = "[[\"1\";\"2\"],[\"3\";\"4\"],[\"5\";\"6\"]]";

        List<List<String>> values = getDataTableValues(value);

        List<Element> columnElements = new ArrayList<>();
        List<Element> tableNameElements = new ArrayList<>();
        List<Element> tableNameColumns = new ArrayList<>();

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();

        Element newDataSet = doc.createElement("NewDataSet");

        Element schema = doc.createElement("xs:schema");
        schema.setAttribute("id", "NewDataSet");
        schema.setAttribute("xmlns:xs", "http://www.w3.org/2001/XMLSchema");
        schema.setAttribute("xmlns:msdata", "urn:schemas-microsoft-com:xml-msdata");

        Element newDataSetElement = doc.createElement("xs:element");
        newDataSetElement.setAttribute("name", "NewDataSet");
        newDataSetElement.setAttribute("msdata:IsDataSet", "true");
        newDataSetElement.setAttribute("msdata:MainDataTable", "TableName");
        newDataSetElement.setAttribute("msdata:UseCurrentLocale", "true");

        Element complexType = doc.createElement("xs:complexType");

        Element choice = doc.createElement("xs:choice");
        choice.setAttribute("minOccurs", "0");
        choice.setAttribute("maxOccurs", "unbounded");

        Element tableNameElement = doc.createElement("xs:element");
        tableNameElement.setAttribute("name", "TableName");

        Element tableNameComplexType = doc.createElement("xs:complexType");

        Element sequence = doc.createElement("xs:sequence");

        tableNameComplexType.appendChild(sequence);
        tableNameElement.appendChild(tableNameComplexType);
        choice.appendChild(tableNameElement);
        complexType.appendChild(choice);
        newDataSetElement.appendChild(complexType);
        schema.appendChild(newDataSetElement);
        newDataSet.appendChild(schema);

        for (int i = 0; i < values.get(0).size(); i++) {
            columnElements.add(doc.createElement("xs:element"));
            columnElements.get(i).setAttribute("name", "Column" + (i + 1));
            columnElements.get(i).setAttribute("type", "xs:string");
            columnElements.get(i).setAttribute("minOccurs", "0");

            sequence.appendChild(columnElements.get(i));
        }

        int rowNumber = 0;
        int columnNumber = 0;
        int col;
        for (List<String> row : values) {
            tableNameElements.add(doc.createElement("TableName"));
            col = 0;
            for (String column : row) {
                tableNameColumns.add(doc.createElement("Column" + (col + 1)));
                tableNameColumns.get(columnNumber).setTextContent(column);
                tableNameElements.get(rowNumber).appendChild(tableNameColumns.get(columnNumber));
                columnNumber++;
                col++;
            }
            newDataSet.appendChild(tableNameElements.get(rowNumber));
            rowNumber++;
        }

        doc.appendChild(newDataSet);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.METHOD, "html");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        String output = writer.getBuffer().toString();

        return StringEscapeUtils.escapeXml11(output)
                .replace("\r", "&#xD;&#xA;")
                .replace("\n", "");
    }

    private List<List<String>> getDataTableValues(String value) {
        String[] rowsString = value.substring(1, value.length() - 1).split("];\\[");
        List<List<String>> values = new ArrayList<>();

        for (String rowString : rowsString) {
            String[] valuesString = rowString
                    .replaceAll("[\\[\\]]", "")
                    .split(";(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

            List<String> row = new ArrayList<>(Arrays.asList(valuesString));
            row = row.stream()
                    .map(elem -> elem.length() > 1 ? elem.substring(1, elem.length() - 1) : elem)
                    .collect(Collectors.toList());
            values.add(row);
        }

        return values;
    }

    public void addCopyRangeAction(String sheetName, String range) {
        Element excelReadRange = doc.createElement("ui:ExcelReadRange");
        excelReadRange.setAttribute("AddHeaders", "False");
        excelReadRange.setAttribute("DataTable", "[dataTable]");
        excelReadRange.setAttribute("DisplayName", "Read Range");
        excelReadRange.setAttribute("Range", getFirstCell(range));
        excelReadRange.setAttribute("SheetName", sheetName);

        doSequence.appendChild(excelReadRange);
    }

    public void addPasteIntoRangeAction(String sheetName, String range) {
        Element excelWriteRange = doc.createElement("ui:ExcelWriteRange");
        excelWriteRange.setAttribute("AddHeaders", "False");
        excelWriteRange.setAttribute("DataTable", "[dataTable]");
        excelWriteRange.setAttribute("DisplayName", "Write Range");
        excelWriteRange.setAttribute("SheetName", sheetName);
        excelWriteRange.setAttribute("StartingCell", getFirstCell(range));

        doSequence.appendChild(excelWriteRange);
    }


    private String getFirstCell(String range) {
        return range.substring(0, range.indexOf(':'));
    }


    public void addNavigateToAction(String url) {
        Element navigateTo = doc.createElement("ui:NavigateTo");
//        navigateTo.setAttribute("Browser", "[currentBrowser]");
        navigateTo.setAttribute("DisplayName", "Navigate To");
        navigateTo.setAttribute("Url", url);

        doSequence.appendChild(navigateTo);
    }

    public void addCreateNewTabAction() {
        Element sendHotKey = doc.createElement("ui:SendHotkey");
        sendHotKey.setAttribute("Activate", "True");
        sendHotKey.setAttribute("DisplayName", "Create New Tab");
        sendHotKey.setAttribute("Key", "t");
        sendHotKey.setAttribute("KeyModifiers", "Ctrl");

        Element browserScope = doc.createElement("ui:BrowserScope");
        browserScope.setAttribute("BrowserType", "Chrome");
        browserScope.setAttribute("DisplayName", "Attach Browser");
        browserScope.setAttribute("Selector", "&lt;html app='chrome.exe' title='New Tab*'/&gt;");

        Element browserScopeBody = doc.createElement("ui:BrowserScope.Body");

        Element activityAction = doc.createElement("ActivityAction");
        activityAction.setAttribute("x:TypeArguments", "x:Object");

        Element activityAction_Argument = doc.createElement("ActivityAction.Argument");

        Element delegateInArgument = doc.createElement("DelegateInArgument");
        delegateInArgument.setAttribute("x:TypeArguments", "x:Object");
        delegateInArgument.setAttribute("Name", "ContextTarget");

        Element sequence = doc.createElement("Sequence");
        sequence.setAttribute("DisplayName", "Do");

        doSequence.appendChild(sendHotKey);

        setDoSequence(sequence);

        activityAction_Argument.appendChild(delegateInArgument);
        activityAction.appendChild(activityAction_Argument);
        activityAction.appendChild(sequence);
        browserScopeBody.appendChild(activityAction);
        browserScope.appendChild(browserScopeBody);
        mainSequence.appendChild(browserScope);
    }

    public void addSelectTabAction(String title) {
        Element browserScope = doc.createElement("ui:BrowserScope");
        browserScope.setAttribute("BrowserType", "Chrome");
        browserScope.setAttribute("DisplayName", "Attach Browser");
        browserScope.setAttribute("Selector", "&lt;html app='chrome.exe' title='" + title + "*'/&gt;");

        Element browserScopeBody = doc.createElement("ui:BrowserScope.Body");

        Element activityAction = doc.createElement("ActivityAction");
        activityAction.setAttribute("x:TypeArguments", "x:Object");

        Element activityAction_Argument = doc.createElement("ActivityAction.Argument");

        Element delegateInArgument = doc.createElement("DelegateInArgument");
        delegateInArgument.setAttribute("x:TypeArguments", "x:Object");
        delegateInArgument.setAttribute("Name", "ContextTarget");

        Element sequence = doc.createElement("Sequence");
        sequence.setAttribute("DisplayName", "Do");

        setDoSequence(sequence);

        activityAction_Argument.appendChild(delegateInArgument);
        activityAction.appendChild(activityAction_Argument);
        activityAction.appendChild(sequence);
        browserScopeBody.appendChild(activityAction);
        browserScope.appendChild(browserScopeBody);
        mainSequence.appendChild(browserScope);
    }
}