package compilador;

public class Main {

    public static void printHelp(){
        System.out.println("The args need to be the souce code file name followed by one or more optional args");
        System.out.println("-l <loglevel>: set the loglevel to ERROR, INFO(default), DEBUG or TRACE");
    }
    
    public static void main(String[] args) {
        if(args.length == 0){
            printHelp();
            return;
        }
        
        String fileName = args[0];
        byte loglevel = Logger.INFO;
        for(int i = 1; i < args.length; i++) {
            switch (args[i]) {
                case "-l":
                    if(i + 1 == args.length){
                        System.out.println("Expected loglevel after -l");
                        return;
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
                            return;
                    }
                    break;
            
                default:
                    break;
            }
        }

        Logger logger = new Logger(loglevel);
        logger.info("Compiling file %s\n", fileName);

        try {
            Scanner scanner = new Scanner(fileName);
            Token nextToken;
            do{
                logger.debug("\nScanning a token");
                nextToken = scanner.scan();
                logger.info("Token scanned: %s\n", nextToken.toString());
            }while(nextToken.kind != Token.EOT);
            
        } catch (Exception e) {
            logger.error("Error: %s\n", e.getMessage());
            if(loglevel >= Logger.DEBUG)
                e.printStackTrace();
        }
    }
}