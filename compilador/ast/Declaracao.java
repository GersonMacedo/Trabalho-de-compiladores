package compilador.ast;

import compilador.Kind;

public class Declaracao extends AST {
    public Identificador i;
    public Kind t;
    public Declaracao d;
    public Declaracao(){
        d = null;
    }
}
