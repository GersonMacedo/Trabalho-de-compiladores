package compilador;

import java.util.HashMap;
import java.util.Map;

import compilador.ast.Declaracao;

public class IdentificationTable {
    Map<String, Declaracao> varMap = new HashMap<String, Declaracao>();

    public Declaracao add(Declaracao d){
        Declaracao previousValue = varMap.put(d.i.n, d);
        return previousValue;
    }

    public Declaracao get(String s){
        return varMap.get(s);
    }
}
