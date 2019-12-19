package com.compiler.builder.UIPathActions;

import com.compiler.builder.UIPathElement;
import com.compiler.builder.Variables;
import org.apache.commons.text.StringEscapeUtils;
import org.w3c.dom.Element;
import com.compiler.transformation.ActionDataTransformer;
import com.compiler.transformation.DataTransformer;
import com.compiler.transformation.Functions;
import com.compiler.transformation.TargetDataTransformer;

public class InvokeCode extends UIPathElement {
    public boolean isElementPresent;
    private Element invokeCode;

    public InvokeCode(Element doSequence, String selector, String input, boolean isTransform) {
        DataTransformer dataTransformer;
        if (isTransform) {
            dataTransformer = new TargetDataTransformer();
        } else {
            dataTransformer = new ActionDataTransformer();
        }

        if (dataTransformer.isDataTransformationPresent(selector)) {
            isElementPresent = true;
            String code = generateCode(selector, dataTransformer, input, isTransform);
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

        Element split = doc.createElement("InArgument");
        split.setAttribute("x:TypeArguments", "s:String[]");
        split.setAttribute("x:Key", Variables.SPLIT);
        args.appendChild(split);

        Element cells = doc.createElement("InArgument");
        cells.setAttribute("x:TypeArguments", "scg:List(x:String)");
        cells.setAttribute("x:Key", Variables.CELLS);
        cells.setTextContent("[" + Variables.CELLS + "]");
        args.appendChild(cells);
    }

    private void createOutArgs(Element args) {
        Element outArgument = doc.createElement("OutArgument");
        outArgument.setAttribute("x:TypeArguments", "x:String");
        outArgument.setAttribute("x:Key", "transformed_value");
        outArgument.setTextContent("[" + Variables.TRANSFORMED_VALUE + "]");
        args.appendChild(outArgument);
    }

    private String generateCode(String selector, DataTransformer dataTransformer, String input, boolean isTransform) {
        String code = dataTransformer.createTransformation(selector);
        code = Functions.getNewListFunction(input, isTransform) + code;
        return StringEscapeUtils.escapeXml11(code).replaceAll("\n", "&#xD;&#xA;");
    }

    @Override
    public Element getDomElement() {
        return invokeCode;
    }
}
