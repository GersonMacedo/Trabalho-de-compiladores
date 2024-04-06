package compilador;

public class Main {
    public static void main(String[] args) {
        new ArgsParser(args);
        Logger logger = new Logger();
        logger.info("Compiling file %s\n", ArgsParser.fileName);

        try {
            Scanner scanner = new Scanner();
            Token nextToken;

            do {
                logger.debug("\nScanning a token");
                nextToken = scanner.scan();
                logger.info("Token scanned: %s\n", nextToken.toString());
            } while (nextToken.kind != Token.EOT);
        } catch (Exception e) {
            logger.error("Error: %s\n", e.getMessage());
            if (ArgsParser.loglevel >= Logger.DEBUG)
                e.printStackTrace();
            System.exit(2);
        }
    }
}