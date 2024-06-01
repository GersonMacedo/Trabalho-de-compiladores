package compilador;

import compilador.ast.ComandoAtribuicao;
import compilador.ast.ComandoCondicional;
import compilador.ast.ComandoIterativo;
import compilador.ast.ComandoLista;
import compilador.ast.Declaracao;
import compilador.ast.ExpressaoBool;
import compilador.ast.ExpressaoId;
import compilador.ast.ExpressaoInt;
import compilador.ast.ExpressaoSimples;
import compilador.ast.Identificador;
import compilador.ast.Programa;

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
