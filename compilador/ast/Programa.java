package compilador.ast;

import compilador.Visitor;

public class Programa extends AST {
    public String i;
    public Declaracao d;
    public Comando c;

    public Object visit(Visitor v, Object... args){
        return v.visitPrograma(this, args);
    }
}
