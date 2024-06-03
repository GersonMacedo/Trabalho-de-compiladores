package compilador.ast;

import compilador.Visitor;

public class ExpressaoId extends Expressao{
    public Identificador i;

    public ExpressaoId(Identificador i){
        this.i = i;
        this.line = i.line;
        this.column = i.column;
    }

    public Object visit(Visitor v, Object... args){
        return v.visitExpressaoId(this, args);
    }
}
