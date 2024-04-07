package compilador;

public class Main {
    private static void runLexicalAnalysis(){
        Logger logger = new Logger();
        logger.log("\nPerforming lexical analysis at %s\n", ArgsParser.fileName);
        int errors = 0;
        try {
            Scanner scanner = new Scanner();
            Token nextToken;

            do {
                logger.debug("\nScanning a token");
                nextToken = scanner.scan();
                logger.info("Token scanned: %s\n", nextToken.toString());
            } while (nextToken.kind != Kind.EOT);

            errors = scanner.errors;
        } catch (Exception e) {
            logger.error("Error: %s\n", e.getMessage());
            if (ArgsParser.loglevel >= Logger.DEBUG)
                e.printStackTrace();
            System.exit(2);
        }

        if(errors != 0){
            logger.log("%d error%s found\n", errors, (errors > 1 ? "s" : ""));
            System.exit(2);
        }

        logger.log("No lexical errors found");
        System.exit(0);
    }

    private static void runSyntacticAnalysis(){
        Logger logger = new Logger();
        Parser parser;
        logger.log("\nPerforming syntactic analysis at %s\n", ArgsParser.fileName);

        try {
            parser = new Parser();
            parser.parse();
        } catch (Exception e) {
            logger.error("Error: %s\n", e.getMessage());
            if (ArgsParser.loglevel >= Logger.DEBUG)
                e.printStackTrace();
            System.exit(3);
            return; // Dumb compiler
        }

        if(parser.errors != 0){
            logger.log("%d errors found\n", parser.errors);
            System.exit(3);
        }

        logger.log("No syntactic errors found");
        System.exit(0);
    }

    public static void main(String[] args) {
        new ArgsParser(args);

        if(ArgsParser.step == ArgsParser.LEXICAL){
            runLexicalAnalysis();
        }

        if(ArgsParser.step == ArgsParser.SYNTACTIC){
            runSyntacticAnalysis();
        }

        // TODO
    }
}