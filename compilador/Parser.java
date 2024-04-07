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
        while (currentToken.kind == Token.SEMICOLON) {
            acceptIt();
            parseSigleCommand();
        }
    }
    
    private void parseSigleCommand(){
        logger.debug("parseSigleCommand()");
        switch (currentToken.kind) {
        case Token.IDENTIFIER:
            parseIdentifier();
            switch (currentToken.kind) {
            case Token.BECOMES:
                acceptIt();
                parseExpression();
                break;
            case Token.LPAREN:
                acceptIt();
                parseExpression();
                accept(Token.RPAREN);
                break;
            default:
                handleUnexpectedToken();
                break;
            }
            break;
        case Token.IF:
            acceptIt();
            parseExpression();
            accept(Token.THEN);
            parseSigleCommand();
            accept(Token.ELSE);
            parseSigleCommand();
            break;
        case Token.WHILE:
            acceptIt();
            parseExpression();
            accept(Token.DO);
            parseSigleCommand();
            break;
        case Token.LET:
            acceptIt();
            parseDeclaration();
            accept(Token.IN);
            parseSigleCommand();
            break;
        case Token.BEGIN:
            acceptIt();
            parseCommand();
            accept(Token.END);
            break;
        default:
            handleUnexpectedToken();
            break;
        }
    }

    private void parseExpression(){ // Still need to take precedence into account
        logger.debug("parseExpression()");
        parsePrimaryExpression();
        while(currentToken.kind == Token.OPERATOR){
            acceptIt();
            parsePrimaryExpression();
        }
    }

    private void parsePrimaryExpression(){
        logger.debug("parsePrimaryExpression()");
        switch (currentToken.kind) {
        case Token.INTLITERAL:
            parseIntegerLiteral();
            break;
        case Token.IDENTIFIER:
            acceptIt();
            break;
        case Token.OPERATOR: // Need to accept only unary operators
            acceptIt();
            parsePrimaryExpression();
            break;
        case Token.LPAREN:
            acceptIt();
            parseExpression();
            accept(Token.RPAREN);
            break;
        default:
            handleUnexpectedToken();
            break;
        }
    }

    private void parseDeclaration(){
        logger.debug("parseDeclaration()");
        parseSingleDeclaration();
        while(currentToken.kind == Token.SEMICOLON){
            acceptIt();
            parseSingleDeclaration();
        }
    }

    private void parseSingleDeclaration(){
        logger.debug("parseSingleDeclaration()");
        switch (currentToken.kind) {
        case Token.CONST:
            acceptIt();
            parseIdentifier();
            accept(Token.IS);
            parseExpression();
            break;
        case Token.VAR:
            acceptIt();
            parseIdentifier();
            accept(Token.COLON);
            parseTypeDenoter();
            break;
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
        accept(Token.IDENTIFIER);
    }

    private void parseIntegerLiteral(){
        logger.debug("parseIntegerLiteral()");
        accept(Token.INTLITERAL);
    }

    @SuppressWarnings("unused")
    private void parseOperator(){
        logger.debug("parseOperator()");
    }

    private void acceptIt() {
        logger.debug("acceptIt()");
        currentToken = scanner.scan();
    }

    private void accept(byte expectedKind) {
        logger.debug("accept(%d)\n", expectedKind);
        if(currentToken.kind == Token.ERROR){
            logger.error("%s at line %d column %d\n", currentToken.spelling, currentToken.line, currentToken.column);
            if(ArgsParser.stopAtFirstError){
                System.exit(2);
            }
            errors++;
        }
            
        if(ArgsParser.step >= ArgsParser.LEXICAL && currentToken.kind != expectedKind){
            logger.error("Expected '%s' but '%s' was found at line %d column %d\n", Token.spellings[expectedKind],
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
        if (currentToken.kind != Token.EOT) {
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
