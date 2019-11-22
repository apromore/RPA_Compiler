package compiler.builder.UIPathActions.excel;

import compiler.builder.UIPathElement;
import compiler.entity.Action;
import org.apache.commons.text.StringEscapeUtils;
import org.w3c.dom.Element;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TableInfo extends UIPathElement {
    private Element newDataSetElement;
    private Element schema;
    private Element newDataSet;
    private Element sequence;
    private Action action;

    public TableInfo(Action action) {
        this.action = action;
        createDataSet();
    }

    private static List<List<String>> parseDataTableValues(String range) {
        List<List<String>> dataTableValues = new ArrayList<>();
        String rangeSeparator = "];\\[(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
        String rowSeparator = ";(?=([^\"]*\"[^\"]*\")*[^\"]*$)";

        String[] rows = range.substring(2, range.length() - 2).split(rangeSeparator);
        for (String row : rows) {
            List<String> rowValues = Arrays.asList(row.split(rowSeparator));
            dataTableValues.add(rowValues);
        }

        return dataTableValues;
    }

    public String getTableInfo() throws TransformerException {
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

    private void createDataSet() {
        newDataSet = doc.createElement("NewDataSet");
        createSchema();
        createNewDataSetElement();
        createComplexType();
        List<List<String>> dataTableValues = createColumns();
        fillColumns(dataTableValues);
        doc.appendChild(newDataSet);
    }

    private void createSchema() {
        schema = doc.createElement("xs:schema");
        schema.setAttribute("id", "NewDataSet");
        schema.setAttribute("xmlns:xs", "http://www.w3.org/2001/XMLSchema");
        schema.setAttribute("xmlns:msdata", "urn:schemas-microsoft-com:xml-msdata");
    }

    private void createNewDataSetElement() {
        newDataSetElement = doc.createElement("xs:element");
        newDataSetElement.setAttribute("name", "NewDataSet");
        newDataSetElement.setAttribute("msdata:IsDataSet", "true");
        newDataSetElement.setAttribute("msdata:MainDataTable", "TableName");
        newDataSetElement.setAttribute("msdata:UseCurrentLocale", "true");
    }

    private void createComplexType() {
        Element complexType = doc.createElement("xs:complexType");
        Element choice = doc.createElement("xs:choice");
        Element tableNameElement = doc.createElement("xs:element");
        Element tableNameComplexType = doc.createElement("xs:complexType");
        sequence = doc.createElement("xs:sequence");

        choice.setAttribute("minOccurs", "0");
        choice.setAttribute("maxOccurs", "unbounded");
        tableNameElement.setAttribute("name", "TableName");

        newDataSet.appendChild(schema).appendChild(newDataSetElement).appendChild(complexType).appendChild(choice)
                .appendChild(tableNameElement).appendChild(tableNameComplexType).appendChild(sequence);
    }

    private List<List<String>> createColumns() {
        String targetValue = action.getTargetValue();
        List<List<String>> dataTableValues = parseDataTableValues(targetValue);
        List<Element> columnElements = new ArrayList<>();

        for (int i = 0; i < dataTableValues.get(0).size(); i++) {
            Element element = doc.createElement("xs:element");
            element.setAttribute("name", "Column" + (i + 1));
            element.setAttribute("type", "xs:string");
            element.setAttribute("minOccurs", "0");
            columnElements.add(element);
            sequence.appendChild(columnElements.get(i));
        }

        return dataTableValues;
    }

    private void fillColumns(List<List<String>> dataTableValues) {
        List<Element> tableNameElements = new ArrayList<>();
        List<Element> tableNameColumns = new ArrayList<>();

        for (int i = 0; i < dataTableValues.size(); i++) {
            tableNameElements.add(doc.createElement("TableName"));
            for (int j = 0; j < dataTableValues.get(i).size(); j++) {
                tableNameColumns.add(doc.createElement("Column" + (j + 1)));
                tableNameColumns.get(j).setTextContent(dataTableValues.get(i).get(j));
                tableNameElements.get(i).appendChild(tableNameColumns.get(j));
            }
            newDataSet.appendChild(tableNameElements.get(i));
        }
    }

    @Override
    public Element getDomElement() {
        return newDataSet;
    }
}
