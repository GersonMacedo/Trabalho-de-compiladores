package compilador;

enum Kind{
    IDENTIFIER, INTLITERAL, TRUE, FALSE, BEGIN, END, IF, THEN, ELSE, VAR, COLON, SEMICOLON, LPAREN, RPAREN,
    DOT, WHILE, DO, COMMA, PLUS, MINUS, OR, MULT, DIV, AND, LESS, GREATER, BECOMES, EQUAL, EOT, ERROR;
}

public class Token {
    public Kind kind;
    public String spelling;
    public int line;
    public int column;
    public Token(Kind kind, String spelling, int line, int column) {
        this.kind = kind;
        this.spelling = spelling;
        this.line = line;
        this.column = column;

        Logger logger = new Logger("Token");
        if (kind != Kind.IDENTIFIER){
            logger.debug(toString());
            return;
        }

        // If kind is IDENTIFIER and spelling matches one
        // of the keywords, change the token's kind accordingly:
        for (int k = Kind.BEGIN.ordinal(); k <= Kind.WHILE.ordinal(); k++){
            if (spelling.equals(spellings[k])) {
                this.kind = Kind.values()[k];
                return;
            }
        }
        logger.debug(toString());
    }

    public String toString(){
        if(kind.ordinal() <= Kind.INTLITERAL.ordinal())
            return String.format("Token(\"%s\", %d, %d)", spelling, line, column);
        return String.format("Token(%s, \"%s\", %d, %d)", spellings[kind.ordinal()], spelling, line, column);
    }
    
    // Spellings of different kinds of token (must correspond to the token kinds above):
    public final static String[] spellings = {
        "<identifier>", "<integer-literal>", "true", "false", "begin", "end", "if", "then", "else", "var", ":", ";",
        "(", ")", ".", "while", "do", ",", "+", "-", "or", "*", "/", "and", "<", ">", ":=", "=", "<eot>"
    };
}