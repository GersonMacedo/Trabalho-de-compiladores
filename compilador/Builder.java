package compilador;

import compilador.ast.*;
import compilador.Utils;
import TAM.Instruction;
import TAM.Machine;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Builder implements Visitor {
    private Logger logger;
    private int labelCount;
    private List<Instruction> instructions;
    private int nextInstruction;
    IdentificationTable it;
    public int errors;

    public Builder() {
        logger = new Logger("Builder");
        logger.debug("Builder()");
        it = new IdentificationTable();
        instructions = new ArrayList<Instruction>();
        nextInstruction = 0;
    }

    private void emit(int op, int r, int n, int d){
        Instruction i = new Instruction();
        i.op = op;
        i.r = r;
        i.n = n;
        i.d = d;
        instructions.add(i);
        nextInstruction++;
    }

    private void patch(int a, int d){
        instructions.listIterator(a).next().d = d;
    }

    public String genarateLabel(){
        labelCount++;
        return String.format("L%03d", labelCount);
    }

    private void write(String file){
        logger.log("Writing object to %s\n", file);
        try{
            FileOutputStream objectFile = new FileOutputStream(file);
            DataOutputStream objectStream = new DataOutputStream(objectFile);
            ListIterator<Instruction> it = instructions.listIterator();
            
            while(it.hasNext()){
                it.next().write(objectStream);
            }
            objectFile.close();
        }catch(Exception e){
            logger.error("Error writing the object file");
        }
    } 

    public void build(Programa p){
        p.visit(this);
        write("obj.tam");
    }

    @Override
    public Object visitComandoAtribuicao(ComandoAtribuicao c, Object... args) {
        logger.debug("visitComandoAtribuicao()");
        c.e.visit(this, args);
        c.i.visit(this, "assign");
        return null;
    }

    @Override
    public Object visitComandoCondicional(ComandoCondicional c, Object... args) {
        String labelG= genarateLabel();
        c.e.visit(this);
        int ifAddress = nextInstruction;
        emit(Machine.JUMPIFop, Machine.falseRep, Machine.CBr, 0);
        logger.logCommand("JUMPIF(0) %s\n", labelG);
        c.v.visit(this);
        if(c.f != null){
            String labelH= genarateLabel();
            int jumpAddress = nextInstruction;
            emit(Machine.JUMPop, 0, Machine.CBr, 0);
            logger.logCommand("JUMP      %s\n", labelH);
            patch(ifAddress, nextInstruction);
            logger.setNextLabel(labelG);
            c.f.visit(this);
            patch(jumpAddress, nextInstruction);
            logger.setNextLabel(labelH);
        }else{
            logger.setNextLabel(labelG);
            patch(ifAddress, nextInstruction);
        }
        
        return null;
    }

    @Override
    public Object visitComandoIterativo(ComandoIterativo c, Object... args) {
        logger.debug("visitComandoIterativo()");
        String labelG = genarateLabel();
        String labelH = genarateLabel();
        int jumpAddress = nextInstruction;
        emit(Machine.JUMPop, 0, Machine.CBr, 0);
        logger.logCommand("JUMP      %s\n", labelH);

        int loopAddress = nextInstruction;
        logger.setNextLabel(labelG);
        c.c.visit(this);
        patch(jumpAddress, nextInstruction);

        logger.setNextLabel(labelH);
        c.e.visit(this, args);
        emit(Machine.JUMPIFop, Machine.CBr, Machine.trueRep, loopAddress);
        logger.logCommand("JUMPIF(1) %s\n", labelG);
        return null;
    }

    @Override
    public Object visitComandoPrint(ComandoPrint c, Object... args) {
        logger.debug("visitComandoPrint()");
        c.e.visit(this, args);
        emit(Machine.CALLop, Machine.PBr, 0, Machine.putintDisplacement);
        emit(Machine.CALLop, Machine.PBr, 0, Machine.puteolDisplacement);
        logger.logCommand("PRINT\n");
        return null;
    }

    @Override
    public Object visitComandoLista(ComandoLista c, Object... args) {
        logger.debug("visitComandoLista()");
        c.c1.visit(this);
        if(c.c2 != null)
            c.c2.visit(this);
        return null;
    }

    @Override
    public Object visitDeclaracao(Declaracao d, Object... args) {
        logger.debug("visitDeclaracao()");
        while(d.d != null)
            d = d.d;
        return d.pos + 1;
    }

    @Override
    public Object visitExpressaoBool(ExpressaoBool e, Object... args) {
        logger.debug("visitExpressaoBool()\n");
        int v = e.b? 1: 0;
        emit(Machine.LOADLop, Machine.CBr, 0, v);
        logger.logCommand("LOADL     %d\n", v);
        return null;
    }

    @Override
    public Object visitExpressaoId(ExpressaoId e, Object... args) {
        logger.debug("visitExpressaoId()");
        return e.i.visit(this, "fetch");
    }

    @Override
    public Object visitExpressaoInt(ExpressaoInt e, Object... args) {
        logger.debug("visitExpressaoInt()");
        emit(Machine.LOADLop, Machine.CBr, 0, e.i);
        logger.logCommand("LOADL     %d\n", e.i);
        return null;
    }

    @Override
    public Object visitExpressaoSimples(ExpressaoSimples e, Object... args) {
        logger.debug("visitExpressaoSimples()");
        e.e1.visit(this);
        e.e2.visit(this);
        int machineOp = Utils.kindToMachineOp(e.op);
        if(e.op == Kind.EQ){
            emit(Machine.LOADLop, Machine.CBr, 0, 1);
            logger.logCommand("LOADL     1\n");
        }
        emit(Machine.CALLop, Machine.PBr, 0, machineOp);
        logger.logCommand("CALL      %s\n", e.op.toString().toLowerCase());
        return null;
    }

    @Override
    public Object visitIdentificador(Identificador i, Object... args) {
        logger.debug("visitIdentificador()");
        String t=(String) args[0];
        if(t.equals("assign")){
            emit(Machine.STOREop, Machine.SBr, 1, i.d.pos);
            logger.logCommand("STORE(1)  %-10s #%s\n", i.getAddress(0), i.n);
            return null;
        }
        
        emit(Machine.LOADop, Machine.SBr, 1, i.d.pos);
        logger.logCommand("LOAD(1)   %-10s #%s\n", i.getAddress(0), i.n);
        return null;
    }

    @Override
    public Object visitPrograma(Programa p, Object... args) {
        logger.debug("visitPrograma()");
        int t = 0;
        if(p.d != null){
            t = (int) p.d.visit(this);
            emit(Machine.PUSHop, 0, 0, t);
            logger.logCommand("PUSH      %d\n", t);
        }

        if(p.c != null)
            p.c.visit(this);
        if(t > 0){
            emit(Machine.POPop, 0, 0, t);
            logger.logCommand("POP       %d\n", t);
        }
        emit(Machine.HALTop, 0, 0, 0);
        logger.logCommand("HALT\n");
        return null;
    }
}
