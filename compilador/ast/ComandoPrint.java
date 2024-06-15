package compilador.ast;

import compilador.Visitor;

public class ComandoPrint extends Comando {
    public Expressao e;

    public Object visit(Visitor v, Object... args){
        return v.visitComandoPrint(this, args);
    }
}
