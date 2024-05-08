package compilador.ast;

import compilador.Visitor;

public class ExpressaoInt extends Expressao{
    public int i;

    public ExpressaoInt(int i){
        this.i = i;
    }

    public void visit(Visitor v){
        v.visitExpressaoInt(this);
    }
}
