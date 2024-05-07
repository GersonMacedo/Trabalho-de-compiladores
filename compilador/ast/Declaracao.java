package compilador.ast;

import compilador.Kind;
import compilador.Logger;

public class Declaracao extends AST {
    public Identificador i;
    public Kind t;
    public Declaracao d;
    public Declaracao(){
        d = null;
    }
    
    public void print() {
        Logger logger = new Logger();
        logger.log(1, "'%s': %s\n", i.n, t.toString());
        if(d != null)
            d.print();
    }
}
