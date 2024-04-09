package compilador.ast;

public class ComandoCondicional extends Comando {
    public Expressao e;
    public Comando v, f;
    
    public ComandoCondicional(){
        v = f = null;
    }
}
