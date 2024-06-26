package compilador;

import compilador.ast.*;

public class Parser {
    private Logger logger;
    private Scanner scanner;
    private Token currentToken;

    public static int errors;

    private void handleUnexpectedTokenWithMessage(String s) {
        if(ArgsParser.step < ArgsParser.SYNTACTIC)
            return;
        
        if(currentToken.kind == Kind.EOT){
            logger.error("The file ended but is missing %s", s);
        }else{
            logger.error("Expected %s but '%s' was found at line %d column %d\n", s, currentToken.spelling,
                currentToken.line, currentToken.column);
        }
        if(ArgsParser.stopAtFirstError)
            System.exit(3);
        errors++;
    }

    private void handleUnexpectedToken() {
        if(ArgsParser.step < ArgsParser.SYNTACTIC)
            return;
        
        logger.error("Unexpected '%s' at line %d column %d\n", currentToken.spelling, currentToken.line,
            currentToken.column);
        if(ArgsParser.stopAtFirstError)
            System.exit(3);
        errors++;
    }

    // <programa> ::= program <id> ; <declarações> <comando-composto> .
    private Programa parsePrograma() {
        logger.debug("parsePrograma()");
        Programa p = new Programa();
        p.setPosition(currentToken);
        accept(Kind.PROGRAM);
        p.i = accept(Kind.IDENTIFIER).spelling;
        accept(Kind.SEMICOLON);
        p.d = parseDeclaracoes();
        p.c = parseComandoComposto();
        accept(Kind.DOT);
        return p;
    }

    // <declarações> ::= (<declaração> ;)*
    private Declaracao parseDeclaracoes() {
        logger.debug("parseDeclaracoes()");
        Declaracao pd = null, ud = null;

        while(currentToken.kind != Kind.BEGIN && currentToken.kind != Kind.EOT) {
            Declaracao d = parseDeclaracao();
            accept(Kind.SEMICOLON);
            if(pd == null)
                pd = d;
            else
                ud.d = d;
            ud = d;
        }
        return pd;
    }

    // <declaração> ::= <declaração-de-variável>
    private Declaracao parseDeclaracao() {
        logger.debug("parseDeclaracao()");
        return parseDeclaracaoDeVariavel();
    }

    // <declaração-de-variável> ::= var <id> : <tipo>
    private Declaracao parseDeclaracaoDeVariavel() {
        logger.debug("parseDeclaracaoDeVariavel()");
        Declaracao d = new Declaracao();
        accept(Kind.VAR);
        d.setPosition(currentToken);
        d.i = new Identificador(accept(Kind.IDENTIFIER));
        accept(Kind.COLON);
        d.t = parseTipo();
        return d;
    }

    // <tipo> ::= <tipo-simples> 
    private Kind parseTipo() {
        logger.debug("parseTipo()");
        return parseTipoSimples();
    }

    // <tipo-simples> ::= integer | boolean
    private Kind parseTipoSimples() {
        logger.debug("parseTipoSimples()");
        if(!currentToken.isSimpleType())
            handleUnexpectedTokenWithMessage("'<tipo>'");
        
        return acceptIt().kind;
    }

    // <comando-composto> ::= begin <lista-de-comandos> end
    private Comando parseComandoComposto() {
        logger.debug("parseComandoComposto()");
        Comando c;
        accept(Kind.BEGIN);
        c = parseListaDeComandos();
        accept(Kind.END);
        return c;
    }

    // <lista-de-comandos> ::= (<comando> ;)*
    private Comando parseListaDeComandos() {
        logger.debug("parseListaDeComandos()");
        ComandoLista pc = new ComandoLista();
        ComandoLista uc = pc;
        while (currentToken.kind != Kind.END && currentToken.kind != Kind.EOT) {
            Comando c = parseComando(true);
            accept(Kind.SEMICOLON);
            if(pc.c1 == null) {
                pc.c1 = c;
                continue;
            }
            if(uc.c2 == null) {
                uc.c2 = c;
                continue;
            }
            ComandoLista nc = new ComandoLista(uc.c2, c);
            uc.c2 = nc;
            uc = nc;
        }
        return pc.c2 == null ? pc.c1 : pc;
    }
    
    // <comando> ::= <print>
    //             | <atribuição>
    //             | <conditional>
    //             | <iterativo>
    //             | <comando-composto>
    private Comando parseComando(boolean insideList) {
        logger.debug("parseComando()");
        switch (currentToken.kind) {
        case PRINT:
            return parsePrint();
        case IDENTIFIER:
            return parseAtribuicao();
        case IF:
            return parseCondicional();
        case WHILE:
            return parseIterativo();
        case BEGIN:
            return parseComandoComposto();
        default:
            handleUnexpectedTokenWithMessage(insideList ? "'<comando>' or 'end'" : "<comando>");
            return null;
        }
    }

    // <print> ::= print <expressão>
    private Comando parsePrint(){
        logger.debug("parsePrint()");
        ComandoPrint c = new ComandoPrint();
        accept(Kind.PRINT);
        c.setPosition(currentToken);
        c.e = parseExpressao();
        return c;
    }

    // <atribuição> ::= <variável> := <expressão>
    private Comando parseAtribuicao() {
        logger.debug("parseAtribuicao()");
        ComandoAtribuicao c = new ComandoAtribuicao();
        c.setPosition(currentToken);
        c.i = parseVariavel();
        accept(Kind.BECOMES);
        c.e = parseExpressao();
        return c;
    }

    // <variável> ::= <id>
    private Identificador parseVariavel() {
        logger.debug("parseVariavel()");
        return new Identificador(accept(Kind.IDENTIFIER));
    }

    // <expressão> ::= <expressão-simples> (<op-rel> <expressão-simples> | <vazio>)
    private Expressao parseExpressao() {
        logger.debug("parseExpressao()");
        Expressao e1 = parseExpressaoSimples();
        if(!currentToken.isOpRel())
            return e1;
        Token op = acceptIt();
        Expressao e2 = parseExpressaoSimples();
        return new ExpressaoSimples(e1, op, e2);
    }

    // <expressão-simples> ::= <termo> (<op-ad> <termo>)*
    private Expressao parseExpressaoSimples() {
        logger.debug("parseExpressaoSimples()");
        ExpressaoSimples pe = null;
        Expressao e1 = parseTermo();
        while(currentToken.isOpAd()) {
            Token op = acceptIt();
            Expressao e2 = parseTermo();
            if(pe == null){
                pe = new ExpressaoSimples(e1, op, e2);
                continue;
            }
            pe = new ExpressaoSimples(pe, op, e2);
        }

        return (pe == null ? e1 : pe);
    }

    // <termo> ::= <fator> (<op-mul> <fator>)*
    private Expressao parseTermo() {
        logger.debug("parseTermo()");
        ExpressaoSimples pe = null;
        Expressao e1 = parseFator();
        while(currentToken.isOpMul()) {
            Token op = acceptIt();
            Expressao e2 = parseFator();
            if(pe == null){
                pe = new ExpressaoSimples(e1, op, e2);
                continue;
            }
            pe = new ExpressaoSimples(pe, op, e2);
        }

        return (pe == null ? e1 : pe);
    }

    // <fator> ::= <variável>
    //           | <literal>
    //           | "(" <expressão> ")"
    private Expressao parseFator() {
        logger.debug("parseFator()");
        if(currentToken.kind == Kind.IDENTIFIER){
            return new ExpressaoId(parseVariavel());
        }

        if(currentToken.isLiteral()){
            return parseLiteral();
        }

        if(currentToken.kind == Kind.LPAREN){
            acceptIt();
            Expressao e = parseExpressao();
            accept(Kind.RPAREN);
            return e;
        }

        handleUnexpectedTokenWithMessage("'<expressao>'");
        return null;
    }

    // <literal> ::= <bool-lit> | <int-lit> 
    private Expressao parseLiteral() {
        logger.debug("parseLiteral()");
        if(!currentToken.isLiteral()){
            handleUnexpectedTokenWithMessage("<literal>");
            return null;
        }

        return acceptIt().toExpressao();
    }
    
    // <condicional> ::= if <expressão> then <comando> ( else <comando> | <vazio> )
    private Comando parseCondicional() {
        logger.debug("parseCondicional()");
        ComandoCondicional c = new ComandoCondicional();
        accept(Kind.IF);
        c.setPosition(currentToken);
        c.e = parseExpressao();
        accept(Kind.THEN);
        c.v = parseComando(false);
        if(currentToken.kind != Kind.ELSE)
            return c;
        
        acceptIt();
        c.f = parseComando(false);
        return c;
    }

    // <iterativo> ::= while <expressão> do <comando>
    private Comando parseIterativo() {
        logger.debug("parseIterativo()");
        ComandoIterativo c = new ComandoIterativo();
        accept(Kind.WHILE);
        c.setPosition(currentToken);
        c.e = parseExpressao();
        accept(Kind.DO);
        c.c = parseComando(false);
        return c;
    }

    private Token acceptIt() {
        logger.debug("acceptIt()");
        if(currentToken.kind == Kind.EOT)
            return currentToken;

        Token previousToken = currentToken;
        currentToken = scanner.scan();
        return previousToken;
    }

    private Token accept(Kind expectedKind) {
        logger.debug("accept(%s)\n", expectedKind.toString());
            
        if(ArgsParser.step >= ArgsParser.SYNTACTIC && currentToken.kind != expectedKind){
            String s;
            if(expectedKind == Kind.VAR){
                s = "'var' or 'begin'";
            }else{
                s = "'" + Token.spellings[expectedKind.ordinal()] + "'";
            }

            handleUnexpectedTokenWithMessage(s);
        }
        
        if(currentToken.kind == Kind.EOT)
            return currentToken;

        Token previousToken = currentToken;
        currentToken = scanner.scan();
        return previousToken;
    }

    public Programa parse(){
        currentToken = scanner.scan();
        Programa p = parsePrograma();

        while (currentToken.kind != Kind.EOT) {
            handleUnexpectedToken();
            acceptIt();
        }

        return p;
    }

    public Parser() {
        logger = new Logger("Parser");
        scanner = new Scanner();
        logger.debug("Parser()");
    }
}
