package compilador.ast;

import compilador.Visitor;

public class ComandoCondicional extends Comando {
    public Expressao e;
    public Comando v, f;
    
    public ComandoCondicional(){
        v = f = null;
    }

    public Object visit(Visitor v, Object... args){
        return v.visitComandoCondicional(this, args);
    }
}
