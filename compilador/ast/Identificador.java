package compilador.ast;

import compilador.Token;
import compilador.Visitor;

public class Identificador extends AST {
    public String n;

    public Identificador(Token t){
        this.n = t.spelling;
    }

    public void visit(Visitor v, Object... args){
        v.visitIdentificador(this, args);
    }
}
