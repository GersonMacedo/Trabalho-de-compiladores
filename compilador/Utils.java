package compilador;

import TAM.Machine;

public class Utils {
    static public int kindToMachineOp(Kind k){
        switch (k) {
            case EQ:
                return Machine.eqDisplacement;
            case ADD:
                return Machine.addDisplacement;
            case SUB:
                return Machine.subDisplacement;
            case MULT:
                return Machine.multDisplacement;
            case DIV:
                return Machine.divDisplacement;
            case LT:
                return Machine.ltDisplacement;
            case GT:
                return Machine.gtDisplacement;
            case OR:
                return Machine.orDisplacement;
            case AND:
                return Machine.andDisplacement;
            default:
                System.out.printf("invalid operation %s\n", k.toString());
                System.exit(4);
                return -1;
        }
    }
}
