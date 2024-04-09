package compilador.ast;

public class ExpressaoId extends Expressao{
    public Identificador i;

    public ExpressaoId(Identificador i){
        this.i = i;
    }
}
