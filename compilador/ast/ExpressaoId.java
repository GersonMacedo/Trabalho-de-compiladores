package compilador.ast;

import compilador.Visitor;

public class ExpressaoId extends Expressao{
    public Identificador i;

    public ExpressaoId(Identificador i){
        this.i = i;
    }

    public void visit(Visitor v, Object... args){
        v.visitExpressaoId(this, args);
    }
}
