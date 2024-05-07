package compilador.ast;

import compilador.Logger;

public class ExpressaoId extends Expressao{
    public Identificador i;

    public ExpressaoId(Identificador i){
        this.i = i;
    }
        
    public void print(int t) {
        Logger logger = new Logger();
        logger.log(t, "$%s\n", i.n);
    }
}
