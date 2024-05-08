package compilador.ast;

import compilador.Kind;
import compilador.Logger;
import compilador.Visitor;

public class Declaracao extends AST {
    public Identificador i;
    public Kind t;
    public Declaracao d;

    public Declaracao(){
        d = null;
    }

    public void visit(Visitor v){
        v.visitDeclaracao(this);
    }
}