package compilador;

import compilador.ast.Programa;

public class Main {
    private static String getStepMessage(){
        switch (ArgsParser.step) {
        case ArgsParser.LEXICAL:
            return "Performing only lexical analysis";
        case ArgsParser.SYNTACTIC:
            return "Performing up to syntactic analysis";
        default:
            throw new UnsupportedOperationException("Unimplemented step " + ArgsParser.step);
        }
    }

    public static void main(String[] args) {
        new ArgsParser(args);
        Logger logger = new Logger();
        logger.log("\n%s at %s\n", getStepMessage(), ArgsParser.fileName);

        Parser parser = new Parser();
        Programa p = parser.parse();
        
        if(Scanner.errors != 0)
            logger.log("%d lexical error%s found\n", Scanner.errors, (Scanner.errors > 1 ? "s" : ""));
        if(Parser.errors != 0)
            logger.log("%d syntactic error%s found\n", Parser.errors, (Parser.errors > 1) ? "s" : "");
        if(Scanner.errors != 0 || Parser.errors != 0)
            System.exit(2);
        logger.log("No errors found");
        p.print();
    }
}