package compilador.ast;

import compilador.Visitor;

public class ComandoIterativo extends Comando {
    public Expressao e;
    public Comando c;

    public ComandoIterativo(){
        c = null;
    }

    public void visit(Visitor v, Object... args){
        v.visitComandoIterativo(this, args);
    }
}
