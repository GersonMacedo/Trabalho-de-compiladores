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
    private int t;

    public void print(Programa p){
        logger.debug("Printing AST");
        logger.setDisplayClass(false);
        p.visit(this);
        logger.setDisplayClass(true);
    }

    public Printer() {
        logger = new Logger("Printer");
        logger.debug("Printer()\n");
        t = 0;
    }

    @Override
    public void visitComandoAtribuicao(ComandoAtribuicao c) {
        logger.log(t, "%s :=\n", c.i.n);
        t++;
        c.e.visit(this);
        t--;
    }

    @Override
    public void visitComandoCondicional(ComandoCondicional c) {
        logger.log(t, "if");
        c.e.visit(this);
        logger.log(t, "then");
        c.v.visit(this);
        if (c.f == null)
            return;
        logger.log(t, "else");
        c.f.visit(this);
    }

    @Override
    public void visitComandoIterativo(ComandoIterativo c) {
        t++;
        logger.log(t-1, "while");
        c.e.visit(this);
        logger.log(t-1, "do");
        c.c.visit(this);
        t--;
    }

    @Override
    public void visitComandoLista(ComandoLista c) {
        c.c1.visit(this);
        if(c.c2 != null)
            c.c2.visit(this);
    }

    @Override
    public void visitDeclaracao(Declaracao d) {
        logger.log(t, "'%s': %s\n", d.i.n, d.t.toString());
        if(d.d != null)
            d.d.visit(this);
    }

    @Override
    public void visitExpressaoBool(ExpressaoBool e) {
        logger.log(t, "%s\n", (e.b ? "true": "false"));
    }

    @Override
    public void visitExpressaoId(ExpressaoId e) {
        logger.log(t, "$%s\n", e.i.n);
    }

    @Override
    public void visitExpressaoInt(ExpressaoInt e) {
        logger.log(t, "%d\n", e.i);
    }

    @Override
    public void visitExpressaoSimples(ExpressaoSimples e) {
        logger.log(t, e.op.toString());
        t++;
        e.e1.visit(this);
        e.e2.visit(this);
        t--;
    }

    @Override
    public void visitIdentificador(Identificador i) {
    }

    @Override
    public void visitPrograma(Programa p) {
        t++;
        logger.log(t-1, "Declarações:");
        if(p.d != null)
            p.d.visit(this);;
        logger.log(t-1, "Corpo:");
        p.c.visit(this);
        t--;
    }
}
