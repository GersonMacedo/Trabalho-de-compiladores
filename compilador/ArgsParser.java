package compilador;

public class ArgsParser {
    public static String fileName;
    public static byte loglevel = Logger.ERROR;
    public static byte step = ArgsParser.LEXICAL;
    public static boolean stopAtFirstError = false;

    private static void printHelp(){
        System.out.println("To compile some file, pass the souce code file name followed by the optional args");
        System.out.println("\t-l <loglevel>: set the loglevel to ERROR(default), INFO, DEBUG or TRACE");
        System.out.println("\t-s <step>: set the to go until LEXICAL(default), SYNTATIC, TREE, CONTENT or BUILD");
        System.out.println("\t--stop-at-first-error to stop when some error is found");
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

            if(args[i].equals("-s")){
                if(i + 1 == args.length){
                    System.out.println("Expected step after -s");
                    System.exit(1);;
                }
                i++;

                switch (args[i]) {
                    case "LEXICAL":
                        step = LEXICAL;
                        break;
                    case "SYNTATIC":
                        step = SYNTATIC;
                        break;
                    case "TREE":
                        step = TREE;
                        break;
                    case "CONTENT":
                        step = CONTENT;
                        break;
                    case "BUILD":
                        step = BUILD;
                        break;
                    default:
                        System.out.println("Invalid step, run without any parameters to see the accepted ones");
                        break;
                }
            }

            if(args[i].equals("--stop-at-first-error")){
                stopAtFirstError = true;
            }
        }
    }

    public final static byte
        LEXICAL = 0, SYNTATIC = 1, TREE = 2, CONTENT = 3, BUILD = 4;
}
