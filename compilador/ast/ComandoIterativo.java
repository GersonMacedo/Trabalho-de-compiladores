package compilador.ast;

import compilador.Visitor;

public class ComandoIterativo extends Comando {
    public Expressao e;
    public Comando c;

    public ComandoIterativo(){
        c = null;
    }

    public Object visit(Visitor v, Object... args){
        return v.visitComandoIterativo(this, args);
    }
}
