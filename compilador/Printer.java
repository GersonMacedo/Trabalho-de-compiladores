package compilador;

import compilador.ast.*;

public class Printer implements Visitor {
    private Logger logger;

    public void print(Programa p){
        logger.debug("Printing AST");
        logger.setDisplayClass(false);
        logger.log("");
        p.visit(this, 0);
        logger.setDisplayClass(true);
    }

    public Printer() {
        logger = new Logger("Printer");
        logger.debug("Printer()\n");
    }

    @Override
    public Object visitComandoAtribuicao(ComandoAtribuicao c, Object... args) {
        int t = (int) args[0];
        logger.log(t, "'%s' :=\n", c.i.n);
        c.e.visit(this, t + 1);
        return null;
    }

    @Override
    public Object visitComandoCondicional(ComandoCondicional c, Object... args) {
        int t = (int) args[0];
        logger.log(t, "if");
        c.e.visit(this, t + 1);
        logger.log(t, "then");
        c.v.visit(this, t + 1);
        if (c.f == null)
            return null;
        logger.log(t, "else");
        c.f.visit(this, t + 1);
        return null;
    }

    @Override
    public Object visitComandoIterativo(ComandoIterativo c, Object... args) {
        int t = (int) args[0];
        logger.log(t, "while");
        c.e.visit(this, t + 1);
        logger.log(t, "do");
        c.c.visit(this, t + 1);
        return null;
    }

    @Override
    public Object visitComandoLista(ComandoLista c, Object... args) {
        int t = (int) args[0];
        c.c1.visit(this, t);
        if(c.c2 != null)
            c.c2.visit(this, t);
        return null;
    }

    @Override
    public Object visitDeclaracao(Declaracao d, Object... args) {
        int t = (int) args[0];
        logger.log(t, "'%s': %s\n", d.i.n, d.t.toString());
        if(d.d != null)
            d.d.visit(this, t);
        return null;
    }

    @Override
    public Object visitExpressaoBool(ExpressaoBool e, Object... args) {
        int t = (int) args[0];
        logger.log(t, "%s\n", (e.b ? "true": "false"));
        return null;
    }

    @Override
    public Object visitExpressaoId(ExpressaoId e, Object... args) {
        e.i.visit(this, args);
        return null;
    }

    @Override
    public Object visitExpressaoInt(ExpressaoInt e, Object... args) {
        int t = (int) args[0];
        logger.log(t, "%d\n", e.i);
        return null;
    }

    @Override
    public Object visitExpressaoSimples(ExpressaoSimples e, Object... args) {
        int t = (int) args[0];
        logger.log(t, e.op.toString());
        e.e1.visit(this, t + 1);
        e.e2.visit(this, t + 1);
        return null;
    }

    @Override
    public Object visitIdentificador(Identificador i, Object... args) {
        int t = (int) args[0];
        logger.log(t, "'%s'\n", i.n);
        return null;
    }

    @Override
    public Object visitPrograma(Programa p, Object... args) {
        int t = (int) args[0];
        logger.log(t, "Programa '%s'\n", p.i);
        logger.log(t, "Declarações:");
        if(p.d != null)
            p.d.visit(this, t + 1);;
        logger.log(t, "Corpo:");
        if(p.c != null)
            p.c.visit(this, t + 1);
        return null;
    }
}
