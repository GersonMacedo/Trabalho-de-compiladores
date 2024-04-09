package compilador;

public enum Kind{
    IDENTIFIER,  // <id>
    INTLITERAL,  // <int-lit>
    TRUE,        // true
    FALSE,       // false
    BEGIN,       // begin
    END,         // end
    IF,          // if
    THEN,        // then
    ELSE,        // else
    VAR,         // var
    COLON,       // :
    SEMICOLON,   // ;
    LPAREN,      // (
    RPAREN,      // )
    DOT,         // .
    WHILE,       // while
    DO,          // do
    COMMA,       // ,
    PLUS,        // +
    MINUS,       // -
    OR,          // or
    MULT,        // *
    DIV,         // /
    AND,         // and
    LESS,        // <
    GREATER,     // >
    BECOMES,     // :=
    EQUAL,       // =
    PROGRAM,     // program
    INTEGER,     // integer
    BOOLEAN,     // boolean
    EOT,         // <eot>
    ERROR;       // <error>
}
