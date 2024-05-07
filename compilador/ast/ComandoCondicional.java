package compilador.ast;

import compilador.Logger;

public class ComandoCondicional extends Comando {
    public Expressao e;
    public Comando v, f;
    
    public ComandoCondicional(){
        v = f = null;
    }

    public void print(int t) {
        Logger logger = new Logger();
        logger.log(t, "if");
        e.print(t + 1);
        logger.log(t, "then");
        v.print(t + 1);
        if (f == null)
            return;
        logger.log(t, "else");
        f.print(t + 1);
    }
}
