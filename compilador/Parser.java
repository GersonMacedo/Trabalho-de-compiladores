package compilador;

import java.io.IOException;

public class Parser {
    Logger logger;
    private Scanner scanner;
    private Token currentToken;
    public int errors;

    private void handleUnexpectedToken() {
        logger.error("Unexpected '%s' at line %d column %d\n", currentToken.spelling, currentToken.line,
            currentToken.column);
        if(ArgsParser.stopAtFirstError)
            System.exit(3);
        errors++;
    }

    // <programa> ::= program <id> ; <corpo> .
    private void parsePrograma() {
        logger.debug("parsePrograma()");
        accept(Kind.PROGRAM);
        accept(Kind.IDENTIFIER);
        accept(Kind.SEMICOLON);
        parseCorpo();
    }

    // <corpo> ::= <declarações> <comando-composto>
    private void parseCorpo() {
        logger.debug("parseCorpo()");
        parseDeclaracoes();
        parseComandoComposto();
    }

    // <declarações> ::= (<declaração> ;)*
    private void parseDeclaracoes() {
        logger.debug("parseDeclaracoes()");

        // declarações are always followed by comando-composto that starts with 'begin'
        while(currentToken.kind != Kind.BEGIN) {
            parseDeclaracao();
            accept(Kind.SEMICOLON);
        }
    }

    // <declaração> ::= <declaração-de-variável>
    private void parseDeclaracao() {
        logger.debug("parseDeclaracao()");
        parseDeclaracaoDeVariavel();
    }

    // <declaração-de-variável> ::= var <id> : <tipo>
    private void parseDeclaracaoDeVariavel() {
        accept(Kind.VAR);
        accept(Kind.IDENTIFIER);
        accept(Kind.COLON);
        parseTipo();
    }

    // TODO? Maybe will change
    // <tipo> ::= <tipo-simples> 
    private void parseTipo() {
        parseTipoSimples();
    }

    // <tipo-simples> ::= integer | boolean
    private void parseTipoSimples() {
        if(!currentToken.isSimpleType())
            handleUnexpectedToken();
        
        acceptIt();
    }

    // <comando-composto> ::= begin <lista-de-comandos> end
    private void parseComandoComposto() {
        logger.debug("parseComandoComposto()");
        accept(Kind.BEGIN);
        parseListaDeComandos();
        accept(Kind.END);
    }

    // <lista-de-comandos> ::= (<comando> ;)*
    private void parseListaDeComandos() {
        logger.debug("parseListaDeComandos()");
        while (currentToken.kind != Kind.END) {
            parseComando();
            accept(Kind.SEMICOLON);
        }
    }
    
    // <comando> ::= <atribuição>
    //             | <conditional>
    //             | <iterativo>
    //             | <comando-composto>
    private void parseComando() {
        logger.debug("parseComando()");
        switch (currentToken.kind) {
        case IDENTIFIER:
            parseAtribuicao();
            break;
        case IF:
            parseCondicional();
            break;
        case WHILE:
            parseIterativo();
            break;
        case BEGIN:
            parseComandoComposto();
            break;
        default:
            handleUnexpectedToken();
            break;
        }
    }

    // <atribuição> ::= <variável> := <expressão>
    private void parseAtribuicao() {
        logger.debug("parseAtribuicao()");
        parseVariavel();
        accept(Kind.BECOMES);
        parseExpressao();
    }

    // <variável> ::= <id>
    private void parseVariavel() {
        logger.debug("parseVariavel()");
        accept(Kind.IDENTIFIER);
    }

    // <expressão> ::= <expressão-simples> (<op-rel> <expressão-simples> | <vazio>)
    private void parseExpressao() {
        logger.debug("parseExpressao()");
        parseExpressaoSimples();
        if(!currentToken.isOpRel())
            return;
        acceptIt();
        parseExpressaoSimples();
    }

    // <expressão-simples> ::= <termo> (<op-ad> <termo>)*
    private void parseExpressaoSimples() {
        logger.debug("parseExpressaoSimples()");
        parseTermo();
        while(currentToken.isOpAd()) {
            acceptIt();
            parseTermo();
        }
    }

    // <termo> ::= <fator> (<op-mul> <fator>)*
    private void parseTermo() {
        logger.debug("parseTermo()");
        parseFator();
        while(currentToken.isOpMul()) {
            acceptIt();
            parseFator();
        }
    }

    // <fator> ::= <variável>
    //           | <literal>
    //           | "(" <expressão> ")"
    private void parseFator() {
        logger.debug("parseFator()");
        if(currentToken.kind == Kind.IDENTIFIER){
            parseVariavel();
            return;
        }

        if(currentToken.isLiteral()){
            parseLiteral();
            return;
        }

        if(currentToken.kind == Kind.LPAREN){
            acceptIt();
            parseExpressao();
            accept(Kind.RPAREN);
            return;
        }

        handleUnexpectedToken();
    }

    // <literal> ::= <bool-lit> | <int-lit> 
    private void parseLiteral() {
        logger.debug("parseLiteral()");
        if(!currentToken.isLiteral()){
            handleUnexpectedToken();
            return;
        }

        acceptIt();
    }
    
    // <condicional> ::= if <expressão> then <comando> ( else <comando> | <vazio> )
    private void parseCondicional() {
        logger.debug("parseCondicional()");
        accept(Kind.IF);
        parseExpressao();
        accept(Kind.THEN);
        parseComando();
        if(currentToken.kind != Kind.ELSE)
            return;
        
        acceptIt();
        parseComando();
    }

    // <iterativo> ::= while <expressão> do <comando>
    private void parseIterativo() {
        logger.debug("parseIterativo()");
        accept(Kind.WHILE);
        parseExpressao();
        accept(Kind.DO);
        parseComando();
    }

    private void acceptIt() {
        logger.debug("acceptIt()");
        currentToken = scanner.scan();
    }

    private void accept(Kind expectedKind) {
        logger.debug("accept(%s)\n", expectedKind.toString());
            
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
        parsePrograma();

        while (currentToken.kind != Kind.EOT) {
            handleUnexpectedToken();
            acceptIt();
        }
    }

    public Parser() throws IOException {
        logger = new Logger("Parser", false);
        scanner = new Scanner();
        logger.debug("Parser()");
    }
}
