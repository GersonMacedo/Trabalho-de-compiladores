package compilador;

import compilador.ast.*;

public interface Visitor {
    public void visitComandoAtribuicao(ComandoAtribuicao c, Object... args);
    public void visitComandoCondicional(ComandoCondicional c, Object... args);
    public void visitComandoIterativo(ComandoIterativo c, Object... args);
    public void visitComandoLista(ComandoLista c, Object... args);
    
    public void visitDeclaracao(Declaracao d, Object... args);

    public void visitExpressaoBool(ExpressaoBool e, Object... args);
    public void visitExpressaoId(ExpressaoId e, Object... args);
    public void visitExpressaoInt(ExpressaoInt e, Object... args);
    public void visitExpressaoSimples(ExpressaoSimples e, Object... args);

    public void visitIdentificador(Identificador i, Object... args);

    public void visitPrograma(Programa p, Object... args);
}
