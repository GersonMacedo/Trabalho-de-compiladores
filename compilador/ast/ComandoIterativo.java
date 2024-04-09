package compilador.ast;

public class ComandoIterativo extends Comando {
    public Expressao e;
    public Comando c;

    public ComandoIterativo(){
        c = null;
    }
}
