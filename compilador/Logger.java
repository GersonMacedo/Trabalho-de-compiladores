package compilador;

public class Logger {
    private String className;
    private boolean displayClass = true;
    private boolean disable;

    public Logger(){
        className = "";
        displayClass = true;
        disable = false;
    }

    public Logger(String className){
        this.className = className + "::";
        this.disable = ArgsParser.disableLog.contains(className.toLowerCase()) ^ ArgsParser.invertDisableLog;
    }

    public void setDisplayClass(boolean value){
        displayClass = value;
    }
    
    public void log(int t){
        String s = new String(new char[4 * t]).replace('\0', ' ');
        log(s, 1);
    }

    public void log(String string){
        if(disable)
            return;
        System.out.println((displayClass ? className : "") + string);
    }

    public void log(String format, Object... args){
        if(disable)
            return;
        System.out.printf((displayClass ? className : "") + format, args);
    }

    public void error(String string){
        if(ArgsParser.loglevel >= Logger.ERROR)
            log(string);
    }

    public void error(String format, Object... args){
        if(ArgsParser.loglevel >= Logger.ERROR)
            log(format, args);
    }

    public void info(String string){
        if(ArgsParser.loglevel >= Logger.INFO)
            log(string);
    }
    
    public void log(int t, String string) {
        log(t);
        log(string);
    }

    public void info(String format, Object... args){
        if(ArgsParser.loglevel >= Logger.INFO)
            log(format, args);
    }
    
    public void log(int t, String format, Object... args) {
        log(t);
        log(format, args);
    }

    public void debug(String string){
        if(ArgsParser.loglevel >= Logger.DEBUG)
            log(string);
    }

    public void debug(String format, Object... args){
        if(ArgsParser.loglevel >= Logger.DEBUG)
            log(format, args);
    }

    public void trace(String string){
        if(ArgsParser.loglevel >= Logger.TRACE)
            log(string);
    }

    public void trace(String format, Object... args){
        if(ArgsParser.loglevel >= Logger.TRACE)
            log(format, args);
    }

    public final static byte
        ERROR = 0, INFO = 1, DEBUG = 2, TRACE = 3;
}
