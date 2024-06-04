package compilador;

import compilador.ast.*;

public class Builder implements Visitor {
    private Logger logger;
    private int labelCount;
    IdentificationTable it;
    public int errors;

    public Builder() {
        logger = new Logger("Builder");
        logger.debug("Builder()");
        it = new IdentificationTable();
    }

    public String genarateLabel(){
        labelCount++;
        return String.format("L%03d", labelCount);
    }

    public void build(Programa p){
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
        String labelG= genarateLabel();
        c.e.visit(this);
        logger.log("JUMPIF(0) %s\n", labelG);
        c.v.visit(this);
        if(c.f != null){
            String labelH= genarateLabel();
            logger.logCommand("JUMP %s\n", labelH);
            logger.setNextLabel(labelG);
            c.f.visit(this);
            logger.setNextLabel(labelH);
        }else{
            logger.setNextLabel(labelG);
        }
        return null;
    }

    @Override
    public Object visitComandoIterativo(ComandoIterativo c, Object... args) {
        logger.debug("visitComandoIterativo()");
        String labelG= genarateLabel();
        String labelH= genarateLabel();
        logger.logCommand("JUMP %s\n", labelH);
        logger.setNextLabel(labelG);
        c.c.visit(this);
        logger.setNextLabel(labelH);
        c.e.visit(this, args);
        logger.logCommand("JUMPIF(1) %s\n", labelG);
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
        logger.logCommand("LOADL %s\n", e.b?"TRUE":"FALSE");
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
        logger.logCommand("LOADL %d\n", e.i);
        return null;
    }

    @Override
    public Object visitExpressaoSimples(ExpressaoSimples e, Object... args) {
        logger.debug("visitExpressaoSimples()");
        e.e1.visit(this);
        e.e2.visit(this);
        logger.logCommand("CALL %s\n", e.op.toString().toLowerCase());
        return null;
    }

    @Override
    public Object visitIdentificador(Identificador i, Object... args) {
        logger.debug("visitIdentificador()");
        String t=(String) args[0];
        if(t.equals("assign")){
            logger.logCommand("STORE %s\n", i.n);
        }else{
            logger.logCommand("LOAD %s\n", i.n);
        }
        return null;
    }

    @Override
    public Object visitPrograma(Programa p, Object... args) {
        logger.debug("visitPrograma()");
        if(p.c != null)
            p.c.visit(this);
        logger.logCommand("HALT\n");
        return null;
    }
}
