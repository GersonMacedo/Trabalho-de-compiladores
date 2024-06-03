package compilador.ast;

import compilador.Visitor;

public class ExpressaoInt extends Expressao{
    public int i;

    public ExpressaoInt(int i){
        this.i = i;
    }

    public Object visit(Visitor v, Object... args){
        return v.visitExpressaoInt(this, args);
    }
}
