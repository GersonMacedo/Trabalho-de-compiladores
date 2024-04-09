package compilador.ast;

public abstract class Comando extends AST{
    public Comando pc;
    public Comando(){
        pc = null;
    }
}