package compilador;

public class Logger {
    public static byte loglevel;
    public String className;

    public Logger(byte loglevel){
        Logger.loglevel = loglevel;
        className = "";
        debug("Logger started with loglevel: %d\n", loglevel);
    }

    public Logger(String className){
        this.className = className + "::";
    }

    private void log(String string){
        System.out.println(className + string);
    }

    private void log(String format, Object... args){
        System.out.printf(className + format, args);
    }

    public void error(String string){
        if(loglevel >= Logger.ERROR)
            log(string);
    }

    public void error(String format, Object... args){
        if(loglevel >= Logger.ERROR)
            log(format, args);
    }

    public void info(String string){
        if(loglevel >= Logger.INFO)
            log(string);
    }

    public void info(String format, Object... args){
        if(loglevel >= Logger.INFO)
            log(format, args);
    }

    public void debug(String string){
        if(loglevel >= Logger.DEBUG)
            log(string);
    }

    public void debug(String format, Object... args){
        if(loglevel >= Logger.DEBUG)
            log(format, args);
    }

    public void trace(String string){
        if(loglevel >= Logger.TRACE)
            log(string);
    }

    public void trace(String format, Object... args){
        if(loglevel >= Logger.TRACE)
            log(format, args);
    }

    public final static byte
        ERROR = 0, INFO = 1, DEBUG = 2, TRACE = 3;
}
