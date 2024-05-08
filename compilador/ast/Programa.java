package compilador.ast;

import compilador.Visitor;

public class Programa extends AST {
    public Declaracao d;
    public Comando c;

    public void visit(Visitor v){
        v.visitPrograma(this);
    }
}
