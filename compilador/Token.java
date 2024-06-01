package compilador;

import compilador.ast.Expressao;
import compilador.ast.ExpressaoBool;
import compilador.ast.ExpressaoInt;

public class Token {
    public Kind kind;
    public String spelling;
    public int line;
    public int column;

    public Token(Kind kind, String spelling, int line, int column) {
        this.kind = kind;
        this.spelling = spelling.toLowerCase();
        this.line = line;
        this.column = column;

        Logger logger = new Logger("Token");
        if (kind != Kind.IDENTIFIER){
            logger.debug(toString());
            return;
        }

        // If kind is IDENTIFIER and spelling matches one
        // of the keywords, change the token's kind accordingly:
        for (int k = Kind.TRUE.ordinal(); k < Kind.EOT.ordinal(); k++){
            if (this.spelling.equals(spellings[k])) {
                this.kind = Kind.values()[k];
                break;
            }
        }
        logger.debug(toString());
    }

    public boolean isOpAd() {
        switch (kind) {
        case PLUS:
        case MINUS:
        case OR:
            return true;
        default:
            return false;
        }
    }

    public boolean isOpMul() {
        switch (kind) {
        case MULT:
        case DIV:
        case AND:
            return true;
        default:
            return false;
        }
    }

    public boolean isOpRel() {
        switch (kind) {
        case LESS:
        case GREATER:
        case EQUAL:
            return true;
        default:
            return false;
        }
    }

    public boolean isOperator() {
        return isOpAd() || isOpMul() || isOpRel();
    }

    public boolean isSimpleType() {
        switch (kind) {
        case INTEGER:
        case BOOLEAN:
            return true;
        default:
            return false;
        }
    }

    public boolean isLiteral() {
        switch (kind) {
        case INTLITERAL:
        case TRUE:
        case FALSE:
            return true;
        default:
            return false;
        }
    }

    public String toString(){
        if(kind.ordinal() > Kind.INTLITERAL.ordinal())
            return String.format("Token(%s, %d, %d)", spellings[kind.ordinal()], line, column);
        return String.format("Token(%s, \"%s\", %d, %d)", spellings[kind.ordinal()], spelling, line, column);
    }
    
    // Spellings of different kinds of token (must correspond to the token kinds above):
    public final static String[] spellings = {
        "<identifier>", "<integer-literal>", "true", "false", "begin", "end", "if", "then", "else", "var", ":", ";",
        "(", ")", ".", "while", "do", ",", "+", "-", "or", "*", "/", "and", "<", ">", ":=", "=", "program", "integer",
        "boolean", "<eot>", "<error>"
    };

    Expressao toExpressao(){
        switch (kind) {
        case TRUE:
            return new ExpressaoBool(true);
        case FALSE:
            return new ExpressaoBool(false);
        case INTLITERAL:
            return new ExpressaoInt(Integer.parseInt(spelling));
        default:
            return null;
        }
    }
}