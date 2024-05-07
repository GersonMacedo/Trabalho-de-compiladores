package compilador.ast;

import compilador.Logger;

public class ExpressaoInt extends Expressao{
    public int i;

    public ExpressaoInt(int i){
        this.i = i;
    }
    
    public void print(int t) {
        Logger logger = new Logger();
        logger.log(t, "%d\n", i);
    }
}
