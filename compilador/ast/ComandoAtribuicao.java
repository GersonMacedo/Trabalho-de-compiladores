package compilador.ast;

import compilador.Visitor;

public class ComandoAtribuicao extends Comando {
    public Identificador i;
    public Expressao e;

    public void visit(Visitor v){
        v.visitComandoAtribuicao(this);
    }
}
