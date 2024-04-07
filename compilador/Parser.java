package compilador;

import java.io.IOException;

public class Parser {
    Logger logger;
    private Scanner scanner;
    private Token currentToken;
    public int errors;

    @SuppressWarnings("unused")
    private void parseProgram(){
        logger.debug("parseProgram()");
    }

    @SuppressWarnings("unused")
    private void parseCommand(){
        logger.debug("parseCommand()");
    }
    
    @SuppressWarnings("unused")
    private void parseSigleCommand(){
        logger.debug("parseSigleCommand()");
    }

    @SuppressWarnings("unused")
    private void parseExpression(){
        logger.debug("parseExpression()");
    }

    @SuppressWarnings("unused")
    private void parsePrimaryExpression(){
        logger.debug("parsePrimaryExpression()");
    }

    @SuppressWarnings("unused")
    private void parseDeclaration(){
        logger.debug("parseDeclaration()");
    }

    @SuppressWarnings("unused")
    private void parseSingleDeclaration(){
        logger.debug("parseSingleDeclaration()");
    }

    @SuppressWarnings("unused")
    private void parseTypeDenoter(){
        logger.debug("parseTypeDenoter()");
    }

    @SuppressWarnings("unused")
    private void parseIdentifier(){
        logger.debug("parseIdentifier()");
    }

    @SuppressWarnings("unused")
    private void parseIntegerLiteral(){
        logger.debug("parseIntegerLiteral()");
    }

    @SuppressWarnings("unused")
    private void parseOperator(){
        logger.debug("parseOperator()");
    }

    @SuppressWarnings("unused")
    private void acceptIt() {
        logger.debug("acceptIt()");
        currentToken = scanner.scan();
    }

    @SuppressWarnings("unused")
    private void accept(byte expectedKind) throws Exception{
        logger.debug("accept(%d)\n", expectedKind);
        if(currentToken.kind == Token.ERROR){
            logger.error("%s at line %d column %d\n", currentToken.spelling, currentToken.line, currentToken.column);
            if(ArgsParser.stopAtFirstError){
                System.exit(2);
            }
            errors++;
        }
            
        if(ArgsParser.step >= ArgsParser.LEXICAL && currentToken.kind != expectedKind){
            logger.error("Expeted %s at line %d column %d but %s was found", Token.spellings[expectedKind],
                currentToken.line, currentToken.column, currentToken.spelling);
            if(ArgsParser.stopAtFirstError){
                System.exit(3);
            }
            errors++;
        }
        
        currentToken = scanner.scan();
    }

    public Parser() throws IOException {
        logger = new Logger("Parser");
        scanner = new Scanner();
        logger.info("Parser()");
    }
}
