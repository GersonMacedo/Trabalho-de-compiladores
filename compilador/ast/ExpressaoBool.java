package compilador.ast;

import compilador.Visitor;

public class ExpressaoBool extends Expressao{
    public boolean b;

    public ExpressaoBool(boolean b){
        this.b = b;
    }

    public void visit(Visitor v, Object... args){
        v.visitExpressaoBool(this, args);
    }
}
