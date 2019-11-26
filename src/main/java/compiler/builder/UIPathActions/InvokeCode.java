package compiler.builder.UIPathActions;

import compiler.builder.UIPathElement;
import compiler.builder.Variables;
import org.apache.commons.text.StringEscapeUtils;
import org.w3c.dom.Element;
import transformation.DataTransformer;
import transformation.Functions;

public class InvokeCode extends UIPathElement {
    public boolean isElementPresent;
    private Element invokeCode;

    public InvokeCode(Element doSequence, String selector, String input) {
        DataTransformer dataTransformer = new DataTransformer();
        if (dataTransformer.isDataTransformationPresent(selector)) {
            isElementPresent = true;
            String code = generateCode(selector, dataTransformer, input);
            createInvokeCode(doSequence, code);
        }
    }

    private void createInvokeCode(Element doSequence, String code) {
        invokeCode = doc.createElement("ui:InvokeCode");
        invokeCode.setAttribute("Language", "CSharp");
        invokeCode.setAttribute("Code", code);
        createArgs();
        doSequence.appendChild(invokeCode);
    }

    private void createArgs() {
        Element invokeCodeArguments = doc.createElement("ui:InvokeCode.Arguments");
        createInArgs(invokeCodeArguments);
        createOutArgs(invokeCodeArguments);
        invokeCode.appendChild(invokeCodeArguments);
    }

    private void createInArgs(Element args) {
        Element inClipboard = doc.createElement("InArgument");
        inClipboard.setAttribute("x:TypeArguments", "x:String");
        inClipboard.setAttribute("x:Key", Variables.CLIPBOARD);
        inClipboard.setTextContent("[" + Variables.CLIPBOARD + "]");
        args.appendChild(inClipboard);
    }

    private void createOutArgs(Element args) {
        Element outArgument = doc.createElement("OutArgument");
        outArgument.setAttribute("x:TypeArguments", "x:String");
        outArgument.setAttribute("x:Key", "transformed_value");
        outArgument.setTextContent("[" + Variables.TRANSFORMED_VALUE + "]");
        args.appendChild(outArgument);
    }

    private String generateCode(String selector, DataTransformer dataTransformer, String input) {
        String code = dataTransformer.createTransformation(selector);
        code = Functions.getNewListFunction(input) + code;
        return StringEscapeUtils.escapeXml11(code).replaceAll("\n", "&#xD;&#xA;");
    }

    @Override
    public Element getDomElement() {
        return invokeCode;
    }
}
