package compilador.ast;

import compilador.Visitor;

public class ExpressaoBool extends Expressao{
    public boolean b;

    public ExpressaoBool(boolean b){
        this.b = b;
    }

    public Object visit(Visitor v, Object... args){
        return v.visitExpressaoBool(this, args);
    }
}
