package compilador;

import java.io.*;

public class Scanner{
    private BufferedReader bufferedReader;
    private Logger logger;
    private int currentLine = 1;
    private int currentColumn = 0;
    private char currentChar = '\000';
    private byte currentKind;
    private StringBuffer currentSpelling;

    private String printableChar(char c){
        if(c == '\n')
            return "EOL";
        if(c == '\000')
            return "EOF";
        return String.format("%c", c);
    }

    private void take (char expectedChar) throws Exception {
        logger.debug("take(%s)\n", printableChar(expectedChar));
        if (currentChar != expectedChar) {
            throw new Exception(String.format("Expected '%s' but '%s' was found",
                printableChar(expectedChar), printableChar(currentChar)));
        }

        currentSpelling.append(currentChar);
        logger.trace("currentSpelling is now: %s\n", currentSpelling);
        readNextChar();
    }
    
    private void takeIt () throws IOException {
        logger.debug("takeit()");
        currentSpelling.append(currentChar);
        logger.trace("currentSpelling is now: %s\n", currentSpelling);
        readNextChar();
    }
    
    // Returns true if the character c is a digit
    private boolean isDigit (char c) {
        return c >= '0' && c <= '9';
    }
    
    // Returns true if the character c is a letter.
    private boolean isLetter (char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    // Returns true if the character c is an operator
    private boolean isOperator (char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '<' || c == '>' || c == '=';
    }
    
    // Returns true if the character c is a graphic.
    private boolean isGraphic (char c) {
        return c != '\n' && c != '\000';
    }
    
    private byte scanToken () throws Exception {
        logger.debug("ScanToken()");
        logger.trace("Current char is '%s'\n", printableChar(currentChar));
        if (isLetter(currentChar)) {
            takeIt();
            while (isLetter(currentChar) || isDigit(currentChar))
                takeIt();
            return Token.IDENTIFIER;
        }
        
        if (isDigit(currentChar)) {
            takeIt();
            while (isDigit (currentChar))
                takeIt();
            return Token.INTLITERAL;
        }

        if (isOperator(currentChar)) {
            takeIt();
            return Token.OPERATOR;
        }

        switch (currentChar) {
            case ';':
                takeIt();
                return Token.SEMICOLON;
            case ':':
                takeIt();
                if (currentChar == '=') {
                    takeIt();
                    return Token.BECOMES;
                }
                return Token.COLON;
            case '~':
                takeIt();
                return Token.IS;
            case '(':
                takeIt();
                return Token.LPAREN;
            case')':
                takeIt();
                return Token.RPAREN;
            case '\000':
                return Token.EOT;
            default:
                throw new Exception(String.format("Invalid char '%s'",
                    printableChar(currentChar)));
        }
    }
    
    private void scanSeparator() throws Exception {
        logger.debug("scanSeparator()");
        logger.trace("Current char is '%s'\n", printableChar(currentChar));
        switch (currentChar) {
            case'!': {
                takeIt();
                while (isGraphic(currentChar))
                    takeIt ();
                take('\n');
                break;
            }
            case ' ':
            case '\n':
                takeIt();
                break;
        }
    }

    private void readNextChar() throws IOException{
        logger.debug("readNextChar()");

        if(currentChar == '\n'){
            currentLine++;
            currentColumn = 1;
        }else{
            currentColumn++;
        }
        
        int charCode = bufferedReader.read();
        if(charCode == -1){
            currentChar = '\000';
        }else{
            currentChar = (char) charCode;
        }
        
        logger.debug("Current char is '%s' (Line %d, Column %d)\n",
            printableChar(currentChar), currentLine, currentColumn);
    }
    
    public Token scan() throws Exception  {
        logger.debug("scan()");

        try {
            while (currentChar == '!' || currentChar == ' ' || currentChar == '\n')
                scanSeparator();
            currentSpelling = new StringBuffer("");
            currentKind = scanToken();
        }
        catch (Exception e) {
            int line = currentLine;
            int column = currentColumn;
            takeIt();
            return new Token(Token.ERROR, e.getMessage(), line, column);
        }

        return new Token(currentKind, currentSpelling.toString(), currentLine, currentColumn - currentSpelling.length());
    }

    public Scanner() throws IOException {
        logger = new Logger("Scanner");
        logger.debug("Scanner()");

        bufferedReader = new BufferedReader(new FileReader(ArgsParser.fileName));
        currentSpelling = new StringBuffer("");
        readNextChar();
    }
}