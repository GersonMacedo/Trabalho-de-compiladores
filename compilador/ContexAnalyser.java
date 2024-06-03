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
    public Object visitComandoAtribuicao(ComandoAtribuicao c, Object... args) {
        //TODO
        return null;
    }

    @Override
    public Object visitComandoCondicional(ComandoCondicional c, Object... args) {
        //TODO
        return null;
    }

    @Override
    public Object visitComandoIterativo(ComandoIterativo c, Object... args) {
        //TODO
        return null;
    }

    @Override
    public Object visitComandoLista(ComandoLista c, Object... args) {
        //TODO
        return null;
    }

    @Override
    public Object visitDeclaracao(Declaracao d, Object... args) {
        logger.debug("visitDeclaracao()");

        return null;
    }

    @Override
    public Object visitExpressaoBool(ExpressaoBool e, Object... args) {
        //TODO
        return null;
    }

    @Override
    public Object visitExpressaoId(ExpressaoId e, Object... args) {
        //TODO
        return null;
    }

    @Override
    public Object visitExpressaoInt(ExpressaoInt e, Object... args) {
        //TODO
        return null;
    }

    @Override
    public Object visitExpressaoSimples(ExpressaoSimples e, Object... args) {
        //TODO
        return null;
    }

    @Override
    public Object visitIdentificador(Identificador i, Object... args) {
        //TODO
        return null;
    }

    @Override
    public Object visitPrograma(Programa p, Object... args) {
        logger.debug("visitPrograma()");
        if(p.d != null)
            p.d.visit(this);
        if(p.c != null)
            p.c.visit(this);
        return null;
    }
}
