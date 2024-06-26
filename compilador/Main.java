package compilador;

import TAM.Interpreter;
import compilador.ast.Programa;

public class Main {
    private static String getStepMessage(){
        switch (ArgsParser.step) {
        case ArgsParser.LEXICAL:
            return "only lexical analysis";
        case ArgsParser.SYNTACTIC:
            return "up to syntactic analysis";
        case ArgsParser.TREE:
            return "up to syntactic analysis and printing the AST tree";
        case ArgsParser.CONTENT:
            return "up to content analysis";
        case ArgsParser.BUILD:
            return "content analysis and build generation";
        case ArgsParser.RUN:
            return "build generation and running";
        default:
            throw new UnsupportedOperationException("Unimplemented step " + ArgsParser.step);
        }
    }

    public static void main(String[] args) {
        new ArgsParser(args);
        Logger logger = new Logger();
        logger.log("\nPerforming %s at %s\n", getStepMessage(), ArgsParser.fileName);

        Parser parser = new Parser();
        Programa p = parser.parse();
        
        if(Scanner.errors != 0)
            logger.log("%d lexical error%s found\n", Scanner.errors, (Scanner.errors > 1 ? "s" : ""));
        if(Parser.errors != 0)
            logger.log("%d syntactic error%s found\n", Parser.errors, (Parser.errors > 1) ? "s" : "");
        if(Scanner.errors != 0 || Parser.errors != 0)
            System.exit(2);
        if(ArgsParser.step <= ArgsParser.TREE)
            logger.log("No errors found");
        
        if(ArgsParser.step <= ArgsParser.SYNTACTIC)
            return;
        
        Printer printer = new Printer();
        printer.print(p);

        if(ArgsParser.step == ArgsParser.TREE)
            return;
        
        ContexAnalyser ca = new ContexAnalyser();
        ca.analyse(p);
        if(ca.errors != 0){
            logger.log("%d context errors found\n", ca.errors);
            System.exit(4);
        }
        logger.log("No errors found");

        if(ArgsParser.step == ArgsParser.CONTENT)
            return;
        
        logger.log("Building the program\n");
        Builder b=new Builder();
        b.build(p);

        if(ArgsParser.step == ArgsParser.BUILD)
            return;
        
        Interpreter.main(new String[0]);
    }
}