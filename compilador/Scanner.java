package compilador;

import java.io.*;

public class Scanner{
    private BufferedReader bufferedReader;
    private Logger logger;
    private int currentLine = 1;
    private int currentColumn = 0;
    private char currentChar = '\000';
    private Kind currentKind;
    private StringBuffer currentSpelling;

    public static int errors;

    private String printableChar(char c){
        if(c == '\n')
            return "EOL";
        if(c == '\0')
            return "EOF";
        return String.format("%c", c);
    }

    // private void take (char expectedChar) throws Exception {
    //     logger.debug("take(%s)\n", printableChar(expectedChar));
    //     if (currentChar != expectedChar) {
    //         throw new Exception(String.format("Expected '%s' but '%s' was found",
    //             printableChar(expectedChar), printableChar(currentChar)));
    //     }

    //     currentSpelling.append(currentChar);
    //     logger.trace("currentSpelling is now: %s\n", currentSpelling);
    //     readNextChar();
    // }
    
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
        return c != '\n' && c != '\0';
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
            
            // TODO? show error if next char is letter
            // if(isLetter(currentChar)){
            //     while(isDigit(currentChar) || isLetter(currentChar))
            //         takeIt();
            //     show error
            // }
            return Kind.INTLITERAL;
        }

        switch (currentChar) {
            case '+':
                takeIt();
                return Kind.PLUS;
            case '-':
                takeIt();
                return Kind.MINUS;
            case '*':
                takeIt();
                return Kind.MULT;
            case '/':
                takeIt();
                return Kind.DIV;
            case '<':
                takeIt();
                return Kind.LESS;
            case '>':
                takeIt();
                return Kind.GREATER;
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
            case '\n':
                takeIt();
                break;
        }
    }

    private void readNextChar(){
        logger.debug("readNextChar()");

        if(currentChar == '\n'){
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
        if(currentChar >= 'A' && currentChar <= 'Z')
            currentChar -= ('A' - 'a');
        logger.debug("Current char is '%s' (Line %d, Column %d)\n",
            printableChar(currentChar), currentLine, currentColumn);
    }
    
    public Token scan() {
        logger.debug("scan()");

        while(true){
            while (currentChar == '!' || currentChar == ' ' || currentChar == '\n')
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

    public Scanner() throws IOException {
        logger = new Logger("Scanner");
        logger.debug("Scanner()");

        bufferedReader = new BufferedReader(new FileReader(ArgsParser.fileName));
        currentSpelling = new StringBuffer("");
        readNextChar();
    }
}