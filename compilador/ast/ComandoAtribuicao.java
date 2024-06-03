package compilador.ast;

import compilador.Visitor;

public class ComandoAtribuicao extends Comando {
    public Identificador i;
    public Expressao e;

    public Object visit(Visitor v, Object... args){
        return v.visitComandoAtribuicao(this, args);
    }
}
