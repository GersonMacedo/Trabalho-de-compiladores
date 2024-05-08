package compilador.ast;

import compilador.Visitor;

public class ComandoCondicional extends Comando {
    public Expressao e;
    public Comando v, f;
    
    public ComandoCondicional(){
        v = f = null;
    }

    public void visit(Visitor v){
        v.visitComandoCondicional(this);
    }
}
