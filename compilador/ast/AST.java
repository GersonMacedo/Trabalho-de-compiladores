package compilador.ast;

import compilador.Visitor;

public abstract class AST {
    public abstract void visit(Visitor v);
}
