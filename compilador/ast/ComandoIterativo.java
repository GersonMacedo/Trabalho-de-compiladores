package compilador.ast;

import compilador.Logger;

public class ComandoIterativo extends Comando {
    public Expressao e;
    public Comando c;

    public ComandoIterativo(){
        c = null;
    }

    public void print(int t) {
        Logger logger = new Logger();
        logger.log(t, "while");
        e.print(t + 1);
        logger.log(t, "do");
        c.print(t + 1);
    }
}
