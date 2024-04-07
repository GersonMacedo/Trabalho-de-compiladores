package compilador;

public class Logger {
    private String className;
    private boolean disable;

    public Logger(){
        className = "";
        disable = false;
        debug("Logger started with loglevel: %d\n", ArgsParser.loglevel);
    }

    public Logger(String className){
        this.className = className + "::";
        this.disable = ArgsParser.disableLog.contains(className.toLowerCase());
    }

    public void log(String string){
        if(disable)
            return;
        System.out.println(className + string);
    }

    public void log(String format, Object... args){
        if(disable)
            return;
        System.out.printf(className + format, args);
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

    public void info(String format, Object... args){
        if(ArgsParser.loglevel >= Logger.INFO)
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
