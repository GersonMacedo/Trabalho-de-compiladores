package compilador.ast;

import compilador.Kind;
import compilador.Token;
import compilador.Visitor;

public class ExpressaoSimples extends Expressao{
    public Expressao e1, e2;
    public Kind op;

    public ExpressaoSimples(Expressao e1, Token op, Expressao e2){
        this.e1 = e1;
        this.op = op.kind;
        this.e2 = e2;
        setPosition(op);
    }

    public void visit(Visitor v, Object... args){
        v.visitExpressaoSimples(this, args);
    }
}
