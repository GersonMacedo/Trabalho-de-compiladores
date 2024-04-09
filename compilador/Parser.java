package compilador;

import compilador.ast.*;

public class Parser {
    private Logger logger;
    private Scanner scanner;
    private Token currentToken;

    public static int errors;

    private void handleUnexpectedToken() {
        if(ArgsParser.step < ArgsParser.SYNTACTIC)
            return;
        
        logger.error("Unexpected '%s' at line %d column %d\n", currentToken.spelling, currentToken.line,
            currentToken.column);
        if(ArgsParser.stopAtFirstError)
            System.exit(3);
        errors++;
    }

    // <programa> ::= program <id> ; <corpo> .
    private Programa parsePrograma() {
        logger.debug("parsePrograma()");
        Programa p;
        accept(Kind.PROGRAM);
        accept(Kind.IDENTIFIER);
        accept(Kind.SEMICOLON);
        p = parseCorpo();
        accept(Kind.DOT);
        return p;
    }

    // <corpo> ::= <declarações> <comando-composto>
    private Programa parseCorpo() {
        logger.debug("parseCorpo()");
        Programa p = new Programa();
        p.d = parseDeclaracoes();
        p.c = parseComandoComposto();
        return p;
    }

    // <declarações> ::= (<declaração> ;)*
    private Declaracao parseDeclaracoes() {
        logger.debug("parseDeclaracoes()");
        Declaracao pd = null, ud = null;

        while(currentToken.kind != Kind.BEGIN) {
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
        d.i = new Identificador(accept(Kind.IDENTIFIER));
        accept(Kind.COLON);
        d.t = parseTipo();
        return d;
    }

    // TODO? Maybe will change
    // <tipo> ::= <tipo-simples> 
    private Kind parseTipo() {
        logger.debug("parseTipo()");
        return parseTipoSimples();
    }

    // <tipo-simples> ::= integer | boolean
    private Kind parseTipoSimples() {
        logger.debug("parseTipoSimples()");
        if(!currentToken.isSimpleType())
            handleUnexpectedToken();
        
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
        Comando pc = null, uc = null;
        while (currentToken.kind != Kind.END) {
            Comando c = parseComando();
            accept(Kind.SEMICOLON);
            if(pc == null)
                pc = c;
            else
                uc.pc = c;
            uc = c;
        }
        return pc;
    }
    
    // <comando> ::= <atribuição>
    //             | <conditional>
    //             | <iterativo>
    //             | <comando-composto>
    private Comando parseComando() {
        logger.debug("parseComando()");
        switch (currentToken.kind) {
        case IDENTIFIER:
            return parseAtribuicao();
        case IF:
            return parseCondicional();
        case WHILE:
            return parseIterativo();
        case BEGIN:
            return parseComandoComposto();
        default:
            handleUnexpectedToken();
            return null;
        }
    }

    // <atribuição> ::= <variável> := <expressão>
    private Comando parseAtribuicao() {
        logger.debug("parseAtribuicao()");
        ComandoAtribuicao c = new ComandoAtribuicao();
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
        Kind op = acceptIt().kind;
        Expressao e2 = parseExpressaoSimples();
        return new ExpressaoSimples(e1, op, e2);
    }

    // <expressão-simples> ::= <termo> (<op-ad> <termo>)*
    private Expressao parseExpressaoSimples() {
        logger.debug("parseExpressaoSimples()");
        ExpressaoSimples pe = null, le = null;
        Expressao e1 = parseTermo();
        while(currentToken.isOpAd()) {
            Kind op = acceptIt().kind;
            Expressao e2 = parseTermo();
            if(pe == null){
                pe = le = new ExpressaoSimples(e1, op, e2);
                continue;
            }
            ExpressaoSimples ne = new ExpressaoSimples(le.e2, op, e2);
            le.e2 = ne;
            le = ne;
        }

        return (pe == null ? e1 : pe);
    }

    // <termo> ::= <fator> (<op-mul> <fator>)*
    private Expressao parseTermo() {
        logger.debug("parseTermo()");
        ExpressaoSimples pe = null, le = null;
        Expressao e1 = parseFator();
        while(currentToken.isOpMul()) {
            Kind op = acceptIt().kind;
            Expressao e2 = parseFator();
            if(pe == null){
                pe = le = new ExpressaoSimples(e1, op, e2);
                continue;
            }
            ExpressaoSimples ne = new ExpressaoSimples(le.e2, op, e2);
            le.e2 = ne;
            le = ne;
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

        if(currentToken.kind != Kind.LPAREN){
            handleUnexpectedToken();
            return null;
        }

        acceptIt();
        Expressao e = parseExpressao();
        accept(Kind.RPAREN);
        return e;
    }

    // <literal> ::= <bool-lit> | <int-lit> 
    private Expressao parseLiteral() {
        logger.debug("parseLiteral()");
        if(!currentToken.isLiteral()){
            handleUnexpectedToken();
            return null;
        }

        return acceptIt().toExpressao();
    }
    
    // <condicional> ::= if <expressão> then <comando> ( else <comando> | <vazio> )
    private Comando parseCondicional() {
        logger.debug("parseCondicional()");
        ComandoCondicional c = new ComandoCondicional();
        accept(Kind.IF);
        c.e = parseExpressao();
        accept(Kind.THEN);
        c.v = parseComando();
        if(currentToken.kind != Kind.ELSE)
            return c;
        
        acceptIt();
        c.f = parseComando();
        return c;
    }

    // <iterativo> ::= while <expressão> do <comando>
    private Comando parseIterativo() {
        logger.debug("parseIterativo()");
        ComandoIterativo c = new ComandoIterativo();
        accept(Kind.WHILE);
        c.e = parseExpressao();
        accept(Kind.DO);
        c.c = parseComando();
        return c;
    }

    private Token acceptIt() {
        logger.debug("acceptIt()");
        Token previousToken = currentToken;
        currentToken = scanner.scan();
        return previousToken;
    }

    private Token accept(Kind expectedKind) {
        logger.debug("accept(%s)\n", expectedKind.toString());
            
        if(ArgsParser.step >= ArgsParser.SYNTACTIC && currentToken.kind != expectedKind){
            logger.error("Expected '%s' but '%s' was found at line %d column %d\n", Token.spellings[expectedKind.ordinal()],
                currentToken.spelling, currentToken.line, currentToken.column);
            if(ArgsParser.stopAtFirstError){
                System.exit(3);
            }
            errors++;
        }
        
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
