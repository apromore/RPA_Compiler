package compilator.scope;

import compilator.ScriptCreator;
import compilator.entity.Action;
import org.w3c.dom.Element;


public class ExcelScope implements Scope {

    @Override
    public Element createScope(Action action) {
//        return compilator.ScriptCreator.createExcelScope(action.getTarget_workbookName());
        return ScriptCreator.createExcelScope("C:\\Users\\dvsta\\Downloads\\StudentRecords.xlsx");
    }
}
