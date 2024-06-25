package compilador;

import java.io.*;

public class Scanner{
    private BufferedReader bufferedReader;
    private Logger logger;
    private int currentLine = 1;
    private int currentColumn = 0;
    private char currentChar = '\0';
    private Kind currentKind;
    private StringBuffer currentSpelling;

    public static int errors;

    private String printableChar(char c){
        if(c == '\n' || c== '\r')
            return "EOL";
        if(c == '\0')
            return "EOF";
        if(c == '\t')
            return "TAB";
        return String.format("%c", c);
    }
    
    private void takeIt () {
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
    
    // Returns true if the character c is a graphic.
    private boolean isGraphic (char c) {
        return c != '\n' && c != '\r' && c != '\0';
    }
    
    private Kind scanToken () {
        logger.debug("ScanToken()");
        logger.trace("Current char is '%s'\n", printableChar(currentChar));
        if (isLetter(currentChar)) {
            takeIt();
            while (isLetter(currentChar) || isDigit(currentChar))
                takeIt();
            return Kind.IDENTIFIER;
        }
        
        if (isDigit(currentChar)) {
            takeIt();
            while (isDigit (currentChar))
                takeIt();
            return Kind.INTLITERAL;
        }

        switch (currentChar) {
            case '+':
                takeIt();
                return Kind.ADD;
            case '-':
                takeIt();
                return Kind.SUB;
            case '*':
                takeIt();
                return Kind.MULT;
            case '/':
                takeIt();
                return Kind.DIV;
            case '<':
                takeIt();
                return Kind.LT;
            case '>':
                takeIt();
                return Kind.GT;
            case ';':
                takeIt();
                return Kind.SEMICOLON;
            case ':':
                takeIt();
                if (currentChar == '=') {
                    takeIt();
                    return Kind.BECOMES;
                }
                return Kind.COLON;
            case '(':
                takeIt();
                return Kind.LPAREN;
            case ')':
                takeIt();
                return Kind.RPAREN;
            case '.':
                takeIt();
                return Kind.DOT;
            case ',':
                takeIt();
                return Kind.COMMA;
            case '=':
                takeIt();
                return Kind.EQ;
            case '\0':
                return Kind.EOT;
            default:
                return Kind.ERROR;
        }
    }
    
    private void scanSeparator() {
        logger.debug("scanSeparator()");
        logger.trace("Current char is '%s'\n", printableChar(currentChar));
        switch (currentChar) {
            case'!': 
                takeIt();
                while (isGraphic(currentChar))
                    takeIt();
                break;
            case ' ':
            case '\t':
            case '\n':
            case '\r':
                takeIt();
                break;
        }
    }

    private void readNextChar(){
        logger.debug("readNextChar()");

        if(currentChar == '\n' || currentChar == '\r'){
            currentLine++;
            currentColumn = 1;
        }else{
            currentColumn++;
        }
        
        int charCode;
        try{
            charCode = bufferedReader.read();
        }catch(IOException e){
            logger.error(e.getMessage());
            if(ArgsParser.loglevel >= Logger.DEBUG)
                e.printStackTrace();
            charCode = -1;
        }
        
        currentChar = (charCode == -1 ? '\0' : (char) charCode);
        logger.debug("Current char is '%s' (Line %d, Column %d)\n",
            printableChar(currentChar), currentLine, currentColumn);
    }
    
    public Token scan() {
        logger.debug("scan()");

        while(true){
            while (currentChar == '!' || currentChar == ' ' || currentChar == '\t' || currentChar == '\n' || currentChar == '\r')
                scanSeparator();
            currentSpelling = new StringBuffer("");
            currentKind = scanToken();

            if(currentKind != Kind.ERROR)
                break;
            
            int line = currentLine, column = currentColumn;
            char c = currentChar;
            takeIt();
            logger.error("Invalid char '%s' at line %d column %d\n", printableChar(c), line, column);
            if(ArgsParser.stopAtFirstError){
                System.exit(2);
            }
            errors++;
        }

        return new Token(currentKind, currentSpelling.toString(), currentLine, currentColumn - currentSpelling.length());
    }

    public Scanner() {
        logger = new Logger("Scanner");
        logger.debug("Scanner()");

        try {
            bufferedReader = new BufferedReader(new FileReader(ArgsParser.fileName));
            currentSpelling = new StringBuffer("");
            readNextChar();
        } catch(IOException e) {
            logger.error("Unable to open ArgsParser.fileName");
            currentChar = '\0';
        }
    }
}