package compilador.ast;

import compilador.Logger;

public class ExpressaoBool extends Expressao{
    public boolean b;

    public ExpressaoBool(boolean b){
        this.b = b;
    }
    
    public void print(int t) {
        Logger logger = new Logger();
        logger.log(t, "%s\n", (b ? "true": "false"));
    }
}
