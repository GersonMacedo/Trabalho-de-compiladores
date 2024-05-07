package compilador.ast;

import compilador.Logger;

public class ComandoAtribuicao extends Comando {
    public Identificador i;
    public Expressao e;
    
    public void print(int t) {
        Logger logger = new Logger();
        logger.log(t, "%s :=\n", i.n);
        e.print(t + 1);
    }
}
