package compilador;

import java.io.IOException;

public class Parser {
    Logger logger;
    private Scanner scanner;
    private Token currentToken;
    public int errors;

    private void handleUnexpectedToken(){
        logger.error("Unexpected '%s' at line %d column %d\n", currentToken.spelling, currentToken.line,
            currentToken.column);
        if(ArgsParser.stopAtFirstError)
            System.exit(3);
        errors++;
    }

    private void parseProgram(){
        logger.debug("parseProgram()");
        parseSigleCommand();
    }

    private void parseCommand(){
        logger.debug("parseCommand()");
        parseSigleCommand();
        while (currentToken.kind == Kind.SEMICOLON) {
            acceptIt();
            parseSigleCommand();
        }
    }
    
    private void parseSigleCommand(){
        logger.debug("parseSigleCommand()");
        switch (currentToken.kind) {
        case IDENTIFIER:
            parseIdentifier();
            switch (currentToken.kind) {
            // case BECOMES:
            //     acceptIt();
            //     parseExpression();
            //     break;
            case LPAREN:
                acceptIt();
                parseExpression();
                accept(Kind.RPAREN);
                break;
            default:
                handleUnexpectedToken();
                break;
            }
            break;
        case IF:
            acceptIt();
            parseExpression();
            accept(Kind.THEN);
            parseSigleCommand();
            accept(Kind.ELSE);
            parseSigleCommand();
            break;
        case WHILE:
            acceptIt();
            parseExpression();
            accept(Kind.DO);
            parseSigleCommand();
            break;
        // case LET:
        //     acceptIt();
        //     parseDeclaration();
        //     accept(Kind.IN);
        //     parseSigleCommand();
        //     break;
        case BEGIN:
            acceptIt();
            parseCommand();
            accept(Kind.END);
            break;
        default:
            handleUnexpectedToken();
            break;
        }
    }

    private void parseExpression(){
        logger.debug("parseExpression()");
        parsePrimaryExpression();
        // while(currentToken.kind == Kind.OPERATOR){
        //     acceptIt();
        //     parsePrimaryExpression();
        // }
    }

    private void parsePrimaryExpression(){
        logger.debug("parsePrimaryExpression()");
        switch (currentToken.kind) {
        case INTLITERAL:
            parseIntegerLiteral();
            break;
        case IDENTIFIER:
            acceptIt();
            break;
        // case OPERATOR:
        //     acceptIt();
        //     parsePrimaryExpression();
        //     break;
        case LPAREN:
            acceptIt();
            parseExpression();
            accept(Kind.RPAREN);
            break;
        default:
            handleUnexpectedToken();
            break;
        }
    }

    private void parseDeclaration(){
        logger.debug("parseDeclaration()");
        parseSingleDeclaration();
        while(currentToken.kind == Kind.SEMICOLON){
            acceptIt();
            parseSingleDeclaration();
        }
    }

    private void parseSingleDeclaration(){
        logger.debug("parseSingleDeclaration()");
        switch (currentToken.kind) {
        // case Kind.CONST:
        //     acceptIt();
        //     parseIdentifier();
        //     accept(Token.IS);
        //     parseExpression();
        //     break;
        // case Kind.VAR:
        //     acceptIt();
        //     parseIdentifier();
        //     accept(Kind.COLON);
        //     parseTypeDenoter();
        //     break;
        default:
            handleUnexpectedToken();
            break;
        }
    }

    private void parseTypeDenoter(){
        logger.debug("parseTypeDenoter()");
        parseIdentifier();
    }

    private void parseIdentifier(){
        logger.debug("parseIdentifier()");
        accept(Kind.IDENTIFIER);
    }

    private void parseIntegerLiteral(){
        logger.debug("parseIntegerLiteral()");
        accept(Kind.INTLITERAL);
    }

    @SuppressWarnings("unused")
    private void parseOperator(){
        logger.debug("parseOperator()");
    }

    private void acceptIt() {
        logger.debug("acceptIt()");
        currentToken = scanner.scan();
    }

    private void accept(Kind expectedKind) {
        logger.debug("accept(%d)\n", expectedKind);
            
        if(ArgsParser.step >= ArgsParser.LEXICAL && currentToken.kind != expectedKind){
            logger.error("Expected '%s' but '%s' was found at line %d column %d\n", Token.spellings[expectedKind.ordinal()],
                currentToken.spelling, currentToken.line, currentToken.column);
            if(ArgsParser.stopAtFirstError){
                System.exit(3);
            }
            errors++;
        }
        
        currentToken = scanner.scan();
    }

    public void parse(){
        currentToken = scanner.scan();
        parseProgram();
        if (currentToken.kind != Kind.EOT) {
            handleUnexpectedToken();
            System.exit(3);
        }
    }

    public Parser() throws IOException {
        logger = new Logger("Parser");
        scanner = new Scanner();
        logger.debug("Parser()");
    }
}
