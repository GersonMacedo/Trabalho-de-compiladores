package compilador.ast;

import compilador.Token;

public class Identificador extends AST {
    public String n;

    public Identificador(Token t){
        this.n = t.spelling;
    }
}
