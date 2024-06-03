package compilador.ast;

import compilador.Token;
import compilador.Visitor;

public class Identificador extends AST {
    public String n;

    public Identificador(Token t){
        this.n = t.spelling;
        setPosition(t);
    }

    public Object visit(Visitor v, Object... args){
        return v.visitIdentificador(this, args);
    }
}
