package compilador.ast;

import compilador.Kind;
import compilador.Visitor;

public class Declaracao extends AST {
    public Identificador i;
    public Kind t;
    public Declaracao d;
    public int level = -1;
    public int pos = -1;

    public Declaracao(){
        d = null;
    }

    public void setPosition(int level, int pos){
        this.level = level;
        this.pos = pos;
    }
    
    public Object visit(Visitor v, Object... args){
        return v.visitDeclaracao(this, args);
    }
}