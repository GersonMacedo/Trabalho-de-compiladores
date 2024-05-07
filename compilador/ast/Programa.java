package compilador.ast;

import compilador.Logger;

public class Programa extends AST {
    public Declaracao d;
    public Comando c;
    
    public void print() {
        Logger logger = new Logger();
        logger.log("\nDeclarações:\n", 0);
        if(d != null)
            d.print();
        logger.log("\nCorpo:\n", 0);
        c.print(1);
    }
}
