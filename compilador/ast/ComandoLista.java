package compilador.ast;

public class ComandoLista extends Comando {
    public Comando c1, c2;
    
    public ComandoLista() {
        c1 = c2 = null;
    }

    public ComandoLista(Comando c1, Comando c2) {
        this.c1 = c1;
        this.c2 = c2;
    }
    
    public void print(int t) {
        c1.print(t);
        if(c2 != null)
            c2.print(t);
    }
}
