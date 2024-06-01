package compilador;

import compilador.ast.Comando;
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

public class Printer implements Visitor {
    private Logger logger;

    public void print(Programa p){
        logger.debug("Printing AST");
        logger.setDisplayClass(false);
        p.visit(this, 0);
        logger.setDisplayClass(true);
    }

    public Printer() {
        logger = new Logger("Printer");
        logger.debug("Printer()\n");
    }

    @Override
    public void visitComandoAtribuicao(ComandoAtribuicao c, Object... args) {
        int t = (int) args[0];
        logger.log(t, "%s :=\n", c.i.n);
        c.e.visit(this, t + 1);
    }

    @Override
    public void visitComandoCondicional(ComandoCondicional c, Object... args) {
        int t = (int) args[0];
        logger.log(t, "if");
        c.e.visit(this, t + 1);
        logger.log(t, "then");
        c.v.visit(this, t + 1);
        if (c.f == null)
            return;
        logger.log(t, "else");
        c.f.visit(this, t + 1);
    }

    @Override
    public void visitComandoIterativo(ComandoIterativo c, Object... args) {
        int t = (int) args[0];
        logger.log(t, "while");
        c.e.visit(this, t + 1);
        logger.log(t, "do");
        c.c.visit(this, t + 1);
    }

    @Override
    public void visitComandoLista(ComandoLista c, Object... args) {
        int t = (int) args[0];
        c.c1.visit(this, t);
        if(c.c2 != null)
            c.c2.visit(this, t);
    }

    @Override
    public void visitDeclaracao(Declaracao d, Object... args) {
        int t = (int) args[0];
        logger.log(t, "'%s': %s\n", d.i.n, d.t.toString());
        if(d.d != null)
            d.d.visit(this, t);
    }

    @Override
    public void visitExpressaoBool(ExpressaoBool e, Object... args) {
        int t = (int) args[0];
        logger.log(t, "%s\n", (e.b ? "true": "false"));
    }

    @Override
    public void visitExpressaoId(ExpressaoId e, Object... args) {
        int t = (int) args[0];
        logger.log(t, "$%s\n", e.i.n);
    }

    @Override
    public void visitExpressaoInt(ExpressaoInt e, Object... args) {
        int t = (int) args[0];
        logger.log(t, "%d\n", e.i);
    }

    @Override
    public void visitExpressaoSimples(ExpressaoSimples e, Object... args) {
        int t = (int) args[0];
        logger.log(t, e.op.toString());
        e.e1.visit(this, t + 1);
        e.e2.visit(this, t + 1);
    }

    @Override
    public void visitIdentificador(Identificador i, Object... args) {
    }

    @Override
    public void visitPrograma(Programa p, Object... args) {
        int t = (int) args[0];
        logger.log(t, "Declarações:");
        if(p.d != null)
            p.d.visit(this, t + 1);;
        logger.log(t, "Corpo:");
        p.c.visit(this, t + 1);
    }
}
