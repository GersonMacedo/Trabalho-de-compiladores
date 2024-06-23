package compilador.ast;

import compilador.Token;
import compilador.Visitor;

public class Identificador extends AST {
    public String n;
    public Declaracao d;

    public Identificador(Token t){
        this.n = t.spelling;
        setPosition(t);
    }

    public String getAddress(int currentLevel){
        if(d == null){
            System.out.println("Declaration not set");
            System.exit(4);
        }

        if(d.level == 0)
            return String.format("[%d]SB", d.pos);
        if(d.level == currentLevel)
            return String.format("[%d]LB", d.pos);
        return String.format("TODO");
    }

    public Object visit(Visitor v, Object... args){
        return v.visitIdentificador(this, args);
    }
}
