package compilador.ast;

import compilador.Kind;
import compilador.Visitor;

public class ExpressaoSimples extends Expressao{
    public Expressao e1, e2;
    public Kind op;

    public ExpressaoSimples(Expressao e1, Kind op, Expressao e2){
        this.e1 = e1;
        this.op = op;
        this.e2 = e2;
    }

    public void visit(Visitor v, Object... args){
        v.visitExpressaoSimples(this, args);
    }
}
