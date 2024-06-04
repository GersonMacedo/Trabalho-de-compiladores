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
        Declaracao d = (Declaracao) c.i.visit(this);
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
            logger.error("var '%s' at line %d column %d was previously declared at line %d column %d\n",
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
        logger.debug("visitExpressaoBool()\n");
        return Kind.BOOLEAN;
    }

    @Override
    public Object visitExpressaoId(ExpressaoId e, Object... args) {
        logger.debug("visitExpressaoId()");
        Declaracao d = (Declaracao) e.i.visit(this);
        return d.t;
    }

    @Override
    public Object visitExpressaoInt(ExpressaoInt e, Object... args) {
        logger.debug("visitExpressaoInt()");
        return Kind.INTEGER;
    }

    @Override
    public Object visitExpressaoSimples(ExpressaoSimples e, Object... args) {
        logger.debug("visitExpressaoSimples()");
        Kind t1 = (Kind) e.e1.visit(this);
        Kind t2 = (Kind) e.e2.visit(this);
        switch (e.op) {
            case EQUAL:
                if(t1 != t2){
                    logger.error("%s operation at line %d column %d expects two equal types, but left has %s type and right has %s type\n",
                        e.op.toString(), e.line, e.column, t1.toString(), t2.toString());
                    System.exit(4);
                }
                return Kind.BOOLEAN;
            case PLUS:
            case MINUS:
            case MULT:
            case DIV:
                if(t1 != Kind.INTEGER || t2 != Kind.INTEGER){
                    logger.error("%s operation at line %d column %d expects two %s types, but left has %s type and right has %s type\n",
                        e.op.toString(), e.line, e.column, Kind.INTEGER.toString(), t1.toString(), t2.toString());
                    System.exit(4);
                }
                return Kind.INTEGER;
            case LESS:
            case GREATER:
                if(t1 != Kind.INTEGER || t2 != Kind.INTEGER){
                    logger.error("%s operation at line %d column %d expects two %s types, but left has %s type and right has %s type\n",
                        e.op.toString(), e.line, e.column, Kind.INTEGER.toString(), t1.toString(), t2.toString());
                    System.exit(4);
                }
                return Kind.BOOLEAN;
            case OR:
            case AND:
                if(t1 != Kind.BOOLEAN || t2 != Kind.BOOLEAN){
                    logger.error("%s operation at line %d column %d expects two %s types, but left has %s type and right has %s type\n",
                        e.op.toString(), e.line, e.column, Kind.BOOLEAN.toString(), t1.toString(), t2.toString());
                    System.exit(4);
                }
                return Kind.BOOLEAN;
            default:
                logger.error("invalid operation %s at line %d column %d\n", e.op.toString(), e.line, e.column);
                System.exit(4);
        }
        return null;
    }

    @Override
    public Object visitIdentificador(Identificador i, Object... args) {
        logger.debug("visitIdentificador()");
        Declaracao d = it.get(i.n);
        if(d == null){
            logger.error("var '%s' at line %d column %d was never declared\n", i.n, i.line, i.column);
            System.exit(4);
        }
        return d;
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
