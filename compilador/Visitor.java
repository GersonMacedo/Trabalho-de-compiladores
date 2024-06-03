package compilador;

import compilador.ast.*;

public interface Visitor {
    public Object visitComandoAtribuicao(ComandoAtribuicao c, Object... args);
    public Object visitComandoCondicional(ComandoCondicional c, Object... args);
    public Object visitComandoIterativo(ComandoIterativo c, Object... args);
    public Object visitComandoLista(ComandoLista c, Object... args);
    
    public Object visitDeclaracao(Declaracao d, Object... args);

    public Object visitExpressaoBool(ExpressaoBool e, Object... args);
    public Object visitExpressaoId(ExpressaoId e, Object... args);
    public Object visitExpressaoInt(ExpressaoInt e, Object... args);
    public Object visitExpressaoSimples(ExpressaoSimples e, Object... args);

    public Object visitIdentificador(Identificador i, Object... args);

    public Object visitPrograma(Programa p, Object... args);
}
