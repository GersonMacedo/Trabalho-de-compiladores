package compilador.ast;

import compilador.Token;
import compilador.Visitor;

public abstract class AST {
    public int line, column;

    public void setPosition(Token t){
        line = t.line;
        column = t.column;
    }

    public abstract Object visit(Visitor v, Object... args);
}
