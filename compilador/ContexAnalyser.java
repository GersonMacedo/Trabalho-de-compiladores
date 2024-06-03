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
        logger.debug("visitComandoAtribuicao()");
        Declaracao d = it.get(c.i.n);
        if(d == null){
            logger.error("var '%s' at line %d column %d was never declared\n", c.i.n, c.line, c.column);
            System.exit(4);
        }
        Kind t = (Kind) c.e.visit(this);
        if(t != d.t){
            logger.error("var '%s' at line %d column %d expects a type %s, but expression has a type %s\n",
                c.i.n, c.line, c.column, d.t.toString(), t.toString());
            System.exit(4);
        }
        return null;
    }

    @Override
    public Object visitComandoCondicional(ComandoCondicional c, Object... args) {
        logger.debug("visitComandoCondicional()");
        Kind t = (Kind) c.e.visit(this);
        if(t != Kind.BOOLEAN){
            logger.error("if requires a %s expression at line %d column %d, but expression has a type %s\n",
                Kind.BOOLEAN.toString(),c.line, c.column, t.toString());
            System.exit(4);  
        }
        c.v.visit(this);
        if(c.f != null)
            c.f.visit(this);
        return null;
    }

    @Override
    public Object visitComandoIterativo(ComandoIterativo c, Object... args) {
        logger.debug("visitComandoIterativo()");
        Kind t = (Kind) c.e.visit(this);
        if(t != Kind.BOOLEAN){
            logger.error("while requires a %s expression at line %d column %d, but expression has a type %s\n",
                Kind.BOOLEAN.toString(),c.line, c.column, t.toString());
            System.exit(4);  
        }
        c.c.visit(this);
        return null;
    }

    @Override
    public Object visitComandoLista(ComandoLista c, Object... args) {
        logger.debug("visitComandoLista()");
        c.c1.visit(this);
        if(c.c2 != null)
            c.c2.visit(this);
        return null;
    }

    @Override
    public Object visitDeclaracao(Declaracao d, Object... args) {
        logger.debug("visitDeclaracao()");
        Declaracao pd = it.add(d);
        if(pd != null){
            logger.error("Var '%s' at line %d column %d was previously declared at line %d column %d\n",
                d.i.n, d.line, d.column, pd.line, pd.column);
            System.exit(4);
        }

        if(d.d != null){
            d.d.visit(this);
        }
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
