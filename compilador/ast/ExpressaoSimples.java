package compilador.ast;

import compilador.Kind;
import compilador.Logger;

public class ExpressaoSimples extends Expressao{
    public Expressao e1, e2;
    public Kind op;

    public ExpressaoSimples(Expressao e1, Kind op, Expressao e2){
        this.e1 = e1;
        this.op = op;
        this.e2 = e2;
    }
    
    public void print(int t) {
        Logger logger = new Logger();
        logger.log(t, op.toString());
        e1.print(t + 1);
        e2.print(t + 1);
    }
}
