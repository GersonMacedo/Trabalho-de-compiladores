package compilador;

public class Utils {
    public static int getTypeSize(Kind type){
        switch (type) {
            case BOOLEAN:
                return 1;
            case INTEGER:
                return 2;
            default:
                return 0;
        }
    }
}
