package compilator.scope;

import compilator.entity.Action;
import org.w3c.dom.Element;

public interface Scope {
    public Element createScope(Action action);
}
