package compilador;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ArgsParser {
    public static String fileName;
    public static byte loglevel = Logger.ERROR;
    public static byte step = ArgsParser.CONTENT;
    public static boolean stopAtFirstError = true;
    public static Set<String> disableLog = new HashSet<String>();
    public static boolean invertDisableLog = false;

    public static void printHelp(){
        System.out.println("To compile some file, pass the souce code file name followed by the optional args");
        System.out.println("\t--disable-logs <className>*: disable logs from all the listed <className> class");
        System.out.println("\t--enable-logs <className>*: enable logs only from all the listed <className> class");
        System.out.println("\t-l <loglevel>: set the loglevel to ERROR(default), INFO, DEBUG or TRACE");
        System.out.println("\t-s <step>: set to go until LEXICAL, SYNTATIC, TREE(default), CONTENT or BUILD");
        System.out.println("\t--continue-after-first-error to continue executing after the first error");
    }

    public boolean checkIfFileExists(String path){
        File file = new File(path);
        return file.exists() && file.isFile();
    }

    public ArgsParser(String[] args) {
        if(args.length == 0){
            printHelp();
            System.exit(1);
        }

        fileName = args[0];
        if(!checkIfFileExists(fileName)){
            System.out.printf("The file '%s' doesn't exists\n", fileName);
            System.exit(1);
        }

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
                continue;
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
                    case "SYNTACTIC":
                        step = SYNTACTIC;
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
                        System.exit(1);
                }
                continue;
            }

            if(args[i].equals("--continue-after-first-error")){
                stopAtFirstError = false;
                continue;
            }

            if(args[i].equals("--disable-logs") || args[i].equals("--enable-logs")){
                if(args[i].equals("--enable-logs")){
                    invertDisableLog = true;
                }
                if(i + 1 == args.length){
                    System.out.println("Expected class names after --disble-logs");
                    System.exit(1);;
                }
                i++;
                while(i < args.length && args[i].charAt(0) != '-'){
                    disableLog.add(args[i].toLowerCase());
                    i++;
                }
                continue;
            }

            System.out.printf("Invalid arg '%s', run without any parameters to see the accepted ones\n", args[i]);
            System.exit(1);
        }
    }

    public final static byte
        LEXICAL = 0, SYNTACTIC = 1, TREE = 2, CONTENT = 3, BUILD = 4;
}
