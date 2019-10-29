package compilator.scope;

import compilator.ScriptCreator;
import compilator.entity.Action;
import org.w3c.dom.Element;

public class ChromeScope implements Scope {

    @Override
    public Element createScope(Action action) {
        return ScriptCreator.createBrowserScope();
    }
}
