package compilador.ast;

import compilador.Visitor;

public class ComandoLista extends Comando {
    public Comando c1, c2;
    
    public ComandoLista() {
        c1 = c2 = null;
    }

    public ComandoLista(Comando c1, Comando c2) {
        this.c1 = c1;
        this.c2 = c2;
    }

    public void visit(Visitor v){
        v.visitComandoLista(this);
    }
}
