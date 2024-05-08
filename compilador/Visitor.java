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
    public void visitComandoAtribuicao(ComandoAtribuicao c);
    public void visitComandoCondicional(ComandoCondicional c);
    public void visitComandoIterativo(ComandoIterativo c);
    public void visitComandoLista(ComandoLista c);
    
    public void visitDeclaracao(Declaracao d);

    public void visitExpressaoBool(ExpressaoBool e);
    public void visitExpressaoId(ExpressaoId e);
    public void visitExpressaoInt(ExpressaoInt e);
    public void visitExpressaoSimples(ExpressaoSimples e);

    public void visitIdentificador(Identificador i);

    public void visitPrograma(Programa p);
}
