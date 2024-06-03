package compilador.ast;

import compilador.Kind;
import compilador.Visitor;

public class Declaracao extends AST {
    public Identificador i;
    public Kind t;
    public Declaracao d;

    public Declaracao(){
        d = null;
    }

    public Object visit(Visitor v, Object... args){
        return v.visitDeclaracao(this, args);
    }
}