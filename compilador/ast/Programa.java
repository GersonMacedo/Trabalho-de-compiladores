package compilador.ast;

import compilador.Visitor;

public class Programa extends AST {
    public String i;
    public Declaracao d;
    public Comando c;

    public void visit(Visitor v, Object... args){
        v.visitPrograma(this, args);
    }
}
