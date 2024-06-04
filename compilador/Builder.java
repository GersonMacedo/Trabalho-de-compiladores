package compilador;

import compilador.ast.*;

public class Builder implements Visitor {
    private Logger logger;
    private int tagCount;
    IdentificationTable it;
    public int errors;

    public Builder() {
        logger = new Logger("Builder");
        logger.debug("Builder()");
        it = new IdentificationTable();
    }

    public String getTag(){
        tagCount++;
        return Integer.toString(tagCount);
        
    }

    public void build(Programa p){
        logger.debug("Building the program");
        logger.setDisplayClass(false);
        p.visit(this);
    }

    @Override
    public Object visitComandoAtribuicao(ComandoAtribuicao c, Object... args) {
        logger.debug("visitComandoAtribuicao()");
        c.e.visit(this, args);
        c.i.visit(this, "assign");
        return null;
    }

    @Override
    public Object visitComandoCondicional(ComandoCondicional c, Object... args) {
        logger.debug("visitComandoCondicional()");
        String tagG= getTag();
        c.e.visit(this);
        logger.log("JUMPIF(0) %s\n", tagG);
        c.v.visit(this);
        if(c.f != null){
            String tagH= getTag();
            logger.log("JUMP %s\n", tagH);
            logger.log("%s:",tagG);
            c.f.visit(this);
            logger.log("%s:",tagH);
        }else{
            logger.log("%s:",tagG);
        }
        return null;
    }

    @Override
    public Object visitComandoIterativo(ComandoIterativo c, Object... args) {
        logger.debug("visitComandoIterativo()");
        String tagG= getTag();
        String tagH= getTag();
        logger.log("JUMP %s\n", tagH);
        logger.log("%s:",tagG);
        c.c.visit(this);
        logger.log("%s:",tagH);
        c.e.visit(this, args);
        logger.log("JUMPIF(1) %s\n", tagG);
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
        return null;
    }

    @Override
    public Object visitExpressaoBool(ExpressaoBool e, Object... args) {
        logger.debug("visitExpressaoBool()\n");
        logger.log("LOADL %s\n", e.b?"TRUE":"FALSE");
        return null;
    }

    @Override
    public Object visitExpressaoId(ExpressaoId e, Object... args) {
        logger.debug("visitExpressaoId()");
        e.i.visit(this, "fetch");
        return null;
    }

    @Override
    public Object visitExpressaoInt(ExpressaoInt e, Object... args) {
        logger.debug("visitExpressaoInt()");
        logger.log("LOADL %d\n", e.i);
        return null;
    }

    @Override
    public Object visitExpressaoSimples(ExpressaoSimples e, Object... args) {
        logger.debug("visitExpressaoSimples()");
        e.e1.visit(this);
        e.e2.visit(this);
        logger.log("CALL %s\n", e.op.toString());
        return null;
    }

    @Override
    public Object visitIdentificador(Identificador i, Object... args) {
        logger.debug("visitIdentificador()");
        String t=(String) args[0];
        if(t.equals("assign")){
            logger.log("STORE %s\n", i.n);
        }else{
            logger.log("LOAD %s\n", i.n);
        }
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
