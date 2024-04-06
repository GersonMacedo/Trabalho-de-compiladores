package compilador;

public class ArgsParser {
    public static String fileName;
    public static byte loglevel = Logger.INFO;

    private static void printHelp(){
        System.out.println("The args need to be the souce code file name followed by one or more optional args");
        System.out.println("-l <loglevel>: set the loglevel to ERROR, INFO(default), DEBUG or TRACE");
    }

    public ArgsParser(String[] args) {
        if(args.length == 0){
            printHelp();
            System.exit(1);
        }

        fileName = args[0];
        for(int i = 1; i < args.length; i++) {
            if(args[i].equals("-l")){
                if(i + 1 == args.length){
                    System.out.println("Expected loglevel after -l");
                    System.exit(1);;
                }
                i++;

                switch (args[i]) {
                    case "ERROR":
                        loglevel = Logger.ERROR;
                        break;
                    case "INFO":
                        loglevel = Logger.INFO;
                        break;
                    case "DEBUG":
                        loglevel = Logger.DEBUG;
                        break;
                    case "TRACE":
                        loglevel = Logger.TRACE;
                        break;
                    default:
                        System.out.println("Invalid loglevel, run without any parameters to see the accepted ones");
                        System.exit(1);;
                }
            }
        }
    }
}
