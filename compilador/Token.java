package compilador;

public class Token {
    public byte kind;
    public String spelling;
    public int line;
    public int column;
    public Token(byte kind, String spelling, int line, int column) throws Exception {
        this.kind = kind;
        this.spelling = spelling;
        this.line = line;
        this.column = column;

        if (kind != Token.IDENTIFIER)
            return;

        // If kind is IDENTIFIER and spelling matches one
        // of the keywords, change the token's kind accordingly:
        for (int k = BEGIN; k <= WHILE; k++){
            if (spelling.equals(spellings[k])) {
                this.kind = (byte) k;
                return;
            }
        }
    }

    public String toString(){
        if(spelling.equals(spellings[kind]))
            return String.format("Token(\"%s\", %d, %d)", spelling, line, column);
        return String.format("Token(%s, \"%s\", %d, %d)", spellings[kind], spelling, line, column);
    }
    // Constants denoting different kinds of token:
    public final static byte
        IDENTIFIER = 0, INTLITERAL = 1, OPERATOR = 2, BEGIN = 3, CONST = 4, DO = 5, ELSE = 6,
        END = 7, IF = 8, IN = 9, LET = 10, THEN = 11, VAR = 12, WHILE = 13, SEMICOLON = 14,
        COLON = 15, BECOMES = 16, IS = 17, LPAREN = 18, RPAREN = 19, EOT = 20, ERROR = 21;
    
    // Spellings of different kinds of token (must correspond to the token kinds above):
    private final static String[] spellings = {
        "<identifier>", "<integer-literal>", "<operator>", "begin", "const", "do", "else", "end",
        "if", "in", "let", "then", "var", "while", ";", ":", ":=", "~", "(", ")", "<eot>", "<error>"
    };
}