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

    private void parseTermo() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'parseTermo'");
    }

    private void parseCondicional() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'parseCondicional'");
    }

    private void parseIterativo() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'parseIterativo'");
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
        parsePrograma();

        while (currentToken.kind != Kind.EOT) {
            handleUnexpectedToken();
            acceptIt();
        }
    }

    public Parser() throws IOException {
        logger = new Logger("Parser");
        scanner = new Scanner();
        logger.debug("Parser()");
    }
}
