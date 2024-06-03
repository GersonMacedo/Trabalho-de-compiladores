package compilador;

import compilador.ast.*;

public class ContexAnalyser implements Visitor {
    private Logger logger;
    IdentificationTable it;

    public void analyse(Programa p){
        logger.debug("Analysing context of the program");
        p.visit(this);
    }

    public ContexAnalyser() {
        logger = new Logger("ContexAnalyser");
        logger.debug("ContexAnalyser()");
        it = new IdentificationTable();
    }

    @Override
    public void visitComandoAtribuicao(ComandoAtribuicao c, Object... args) {
        //TODO
    }

    @Override
    public void visitComandoCondicional(ComandoCondicional c, Object... args) {
        //TODO
    }

    @Override
    public void visitComandoIterativo(ComandoIterativo c, Object... args) {
        //TODO
    }

    @Override
    public void visitComandoLista(ComandoLista c, Object... args) {
        //TODO
    }

    @Override
    public void visitDeclaracao(Declaracao d, Object... args) {
        //TODO
    }

    @Override
    public void visitExpressaoBool(ExpressaoBool e, Object... args) {
        //TODO
    }

    @Override
    public void visitExpressaoId(ExpressaoId e, Object... args) {
        //TODO
    }

    @Override
    public void visitExpressaoInt(ExpressaoInt e, Object... args) {
        //TODO
    }

    @Override
    public void visitExpressaoSimples(ExpressaoSimples e, Object... args) {
        //TODO
    }

    @Override
    public void visitIdentificador(Identificador i, Object... args) {
        //TODO
    }

    @Override
    public void visitPrograma(Programa p, Object... args) {
        //TODO
    }
}
